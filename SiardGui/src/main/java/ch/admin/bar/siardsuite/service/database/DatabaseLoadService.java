package ch.admin.bar.siardsuite.service.database;

import ch.admin.bar.siard2.api.Archive;
import ch.admin.bar.siard2.api.Schema;
import ch.admin.bar.siard2.cmd.ArchiveMapping;
import ch.admin.bar.siard2.cmd.MetaDataFromDb;
import ch.admin.bar.siard2.cmd.PrimaryDataFromDb;
import ch.admin.bar.siard2.cmd.PrimaryDataTransfer;
import ch.admin.bar.siardsuite.service.ArchiveHandler;
import ch.admin.bar.siardsuite.service.database.model.LoadDatabaseInstruction;
import ch.admin.bar.siardsuite.service.preferences.UserPreferences;
import ch.admin.bar.siardsuite.ui.presenter.archive.model.CustomArchiveProxy;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.sql.Connection;

@Slf4j
public class DatabaseLoadService extends Service<ObservableList<Pair<String, Long>>> {
    private final DatabaseConnectionFactory connectionFactory;
    private final ArchiveHandler archiveHandler;
    private final UserPreferences userPreferences;

    private final LoadDatabaseInstruction instruction;

    public DatabaseLoadService(DatabaseConnectionFactory connectionFactory, ArchiveHandler archiveHandler, UserPreferences userPreferences, LoadDatabaseInstruction instruction) {
        this.connectionFactory = connectionFactory;
        this.archiveHandler = archiveHandler;
        this.userPreferences = userPreferences;
        this.instruction = instruction;

        this.setOnFailed(instruction.getOnFailure());
        this.valueProperty().addListener(instruction.getOnStepCompleted());
        this.progressProperty().addListener(instruction.getOnProgress());
    }

    @Override
    protected Task<ObservableList<Pair<String, Long>>> createTask() {
        return new DatabaseLoadTask(
                connectionFactory,
                archiveHandler,
                userPreferences,
                instruction
        );
    }

    @RequiredArgsConstructor
    private static class DatabaseLoadTask extends Task<ObservableList<Pair<String, Long>>> {

        private final DatabaseConnectionFactory connectionFactory;
        private final ArchiveHandler archiveHandler;
        private final UserPreferences userPreferences;

        private final LoadDatabaseInstruction instruction;

        @Override
        protected ObservableList<Pair<String, Long>> call() throws Exception {
            val connection = connectionFactory.getOrCreateConnection(instruction.getConnectionData());
            val timeout = userPreferences.getStoredOptions().getQueryTimeout();

            Archive archive = instruction.getSaveAt()
                    .map(archiveHandler::init)
                    .orElseGet(archiveHandler::init);

            val metaDataFromDb = MetaDataFromDb.newInstance(connection.getMetaData(), archive.getMetaData());
            metaDataFromDb.setQueryTimeout(timeout);

            updateValue(FXCollections.observableArrayList(new Pair<>("Metadata", -1L)));
            updateProgress(0, 100);

            // meta data 조회
            metaDataFromDb.download(
                    instruction.getViewsAsTables(),
                    false,
                    new SiardCmdProgressListener(this::updateProgress));

            instruction.getExternalLobs()
                    .ifPresent(uri -> archiveHandler.setExternalLobFolder(archive, uri));

            ObservableList<Pair<String, Long>> progressData = FXCollections.observableArrayList();
            if (!instruction.getLoadOnlyMetadata()) {

//                PrimaryDataFromDb data = PrimaryDataFromDb.newInstance(connection, archive);
                Archive proxy = CustomArchiveProxy.wrap(archive, instruction.getSelectedTables());
                TempPrimaryDataFromDb data = TempPrimaryDataFromDb.newInstance(connection, proxy); // TODO:: 라이브러리로 관리 필요
                data.setQueryTimeout(timeout);
                updateValue(FXCollections.observableArrayList(new Pair<>("Dataload", -1L)));
                updateProgress(0, 100);
                data.download(new SiardCmdProgressListener(this::updateProgress)); // 읽어들인 데이터 다운로드

                if (proxy instanceof CustomArchiveProxy) {
                    CustomArchiveProxy customArchiveProxy = (CustomArchiveProxy) proxy;

                    customArchiveProxy.getSchemaTableMap().forEach(
                            (schema, tables) -> tables.forEach(
                                    t -> {
                                        Pair<String, Long> stringLongPair = new Pair<>(schema.getMetaSchema().getName() + "." + t
                                                .getMetaTable()
                                                .getName(),
                                                t.getMetaTable().getRows());
                                        progressData.add(stringLongPair);
                                    }
                            )
                    );
                } else {
                    for (int i = 0; i < archive.getSchemas(); i++) {
                        Schema schema = archive.getSchema(i);
                        for (int y = 0; y < schema.getTables(); y++) {
                            Pair<String, Long> stringLongPair = new Pair<>(schema.getMetaSchema().getName() + "." + schema.getTable(y)
                                    .getMetaTable()
                                    .getName(),
                                    schema.getTable(y).getMetaTable().getRows());
                            progressData.add(stringLongPair);
                            System.out.println("string long pair = " + stringLongPair);
                        }
                    }
                }
                updateValue(progressData);
            }

            /*
            Workaround: It seems that the default onSucceed mechanism sometimes is not very stable in java fx 8.
            For that reason, the result is returned with a callback.
             */
            Platform.runLater(() -> instruction.getOnSuccess().accept(archive));

            return progressData;
        }
    }

}
