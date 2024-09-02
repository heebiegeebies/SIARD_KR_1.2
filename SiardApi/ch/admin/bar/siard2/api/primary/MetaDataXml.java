package ch.admin.bar.siard2.api.primary;
import ch.admin.bar.siard2.api.generated.CheckConstraintType;
import ch.admin.bar.siard2.api.generated.ColumnType;
import ch.admin.bar.siard2.api.generated.ForeignKeyType;
import ch.admin.bar.siard2.api.generated.MessageDigestType;
import ch.admin.bar.siard2.api.generated.ParameterType;
import ch.admin.bar.siard2.api.generated.PrivOptionType;
import ch.admin.bar.siard2.api.generated.PrivilegeType;
import ch.admin.bar.siard2.api.generated.ReferenceType;
import ch.admin.bar.siard2.api.generated.RoleType;
import ch.admin.bar.siard2.api.generated.RoutineType;
import ch.admin.bar.siard2.api.generated.SchemaType;
import ch.admin.bar.siard2.api.generated.SiardArchive;
import ch.admin.bar.siard2.api.generated.TableType;
import ch.admin.bar.siard2.api.generated.TriggerType;
import ch.admin.bar.siard2.api.generated.UniqueKeyType;
import ch.admin.bar.siard2.api.generated.UserType;
import ch.admin.bar.siard2.api.generated.ViewType;
import ch.admin.bar.siard2.api.generated.old10.ActionTimeType;
import ch.admin.bar.siard2.api.generated.old10.ColumnType;
import ch.admin.bar.siard2.api.generated.old10.ForeignKeyType;
import ch.admin.bar.siard2.api.generated.old10.ParameterType;
import ch.admin.bar.siard2.api.generated.old10.PrivilegeType;
import ch.admin.bar.siard2.api.generated.old10.RoutineType;
import ch.admin.bar.siard2.api.generated.old10.SchemaType;
import ch.admin.bar.siard2.api.generated.old10.SiardArchive;
import ch.admin.bar.siard2.api.generated.old10.TableType;
import ch.admin.bar.siard2.api.generated.old10.TriggerType;
import java.net.URL;
import javax.xml.bind.JAXBException;

public class MetaDataXml {
  private static IndentLogger _il = IndentLogger.getIndentLogger(MetaDataXml.class.getName());
  public static final String sSIARD10_XSD_RESOURCE = "/ch/admin/bar/siard2/api/res/old10/metadata.xsd";
  public static final String sSIARD21_XSD_RESOURCE = "/ch/admin/bar/siard2/api/res/old21/metadata.xsd";
  private static ObjectFactory _of = new ObjectFactory();


  
  private static String convertType(String sType) throws JAXBException {
    if (sType != null) {

      
      sType = sType.toUpperCase().trim();
      BaseSqlFactory baseSqlFactory = new BaseSqlFactory();
      PredefinedType pt = baseSqlFactory.newPredefinedType();
      if (sType.startsWith("BIT"))
      {
        if (sType.equals("BIT")) {
          sType = PreType.BOOLEAN.getKeyword();
        } else {
          
          int iStart = sType.indexOf('(') + 1;
          int iEnd = sType.indexOf(')');
          if (iStart < iEnd) {
            
            String sLength = sType.substring(iStart, iEnd).trim();
            int iLength = Integer.parseInt(sLength);
            iLength = (iLength + 7) / 8;
            if (sType.indexOf("VARYING") >= 0) {
              sType = PreType.VARBINARY.getKeyword();
            } else {
              sType = PreType.BINARY.getKeyword();
            }  sType = sType + "(" + String.valueOf(iLength) + ")";
          } else {
            
            throw new JAXBException("Invalid BIT type: " + sType + "!");
          } 
        }  }  
      try { pt.parse(sType); }
      catch (Exception e) { throw new JAXBException("Type parsing error (" + EU.getExceptionMessage(e) + ")!"); }
       sType = pt.format();
    } 
    return sType;
  }

  
  private static ColumnType convertColumn(ColumnType ctOld) throws JAXBException {
    ColumnType ct = null;
    if (ctOld != null) {
      
      ct = _of.createColumnType();
      ct.setName(ctOld.getName());
      
      ct.setType(convertType(ctOld.getType()));
      ct.setTypeOriginal(ctOld.getTypeOriginal());
      ct.setNullable(Boolean.valueOf(ctOld.isNullable()));
      ct.setDefaultValue(ctOld.getDefaultValue());
      ct.setDescription(ctOld.getDescription());
    } 
    return ct;
  }

  
  private static ColumnsType convertColumns(ColumnsType ctsOld) throws JAXBException {
    ColumnsType cts = null;
    if (ctsOld != null) {
      
      cts = _of.createColumnsType();
      for (int i = 0; i < ctsOld.getColumn().size(); i++) {
        
        ColumnType ct = convertColumn(ctsOld.getColumn().get(i));
        cts.getColumn().add(ct);
      } 
    } 
    return cts;
  }
  
  private static UniqueKeyType convertPrimaryKey(PrimaryKeyType pktOld) {
    UniqueKeyType ukt = null;
    if (pktOld != null) {
      
      ukt = _of.createUniqueKeyType();
      ukt.setName(pktOld.getName());
      ukt.setDescription(pktOld.getDescription());
      for (int i = 0; i < pktOld.getColumn().size(); i++)
        ukt.getColumn().add(pktOld.getColumn().get(i)); 
    } 
    return ukt;
  }
  
  private static ReferenceType convertReference(ReferenceType rtOld) {
    ReferenceType rt = null;
    if (rtOld != null) {
      
      rt = _of.createReferenceType();
      rt.setColumn(rtOld.getColumn());
      rt.setReferenced(rtOld.getReferenced());
    } 
    return rt;
  }
  
  private static ForeignKeyType convertForeignKey(ForeignKeyType fktOld) {
    ForeignKeyType fkt = null;
    if (fktOld != null) {
      
      fkt = _of.createForeignKeyType();
      fkt.setName(fktOld.getName());
      fkt.setReferencedSchema(fktOld.getReferencedSchema());
      fkt.setReferencedTable(fktOld.getReferencedTable());
      for (int i = 0; i < fktOld.getReference().size(); i++) {
        
        ReferenceType rt = convertReference(fktOld.getReference().get(i));
        fkt.getReference().add(rt);
      } 
      if (fktOld.getMatchType() != null) {
        
        String sMatchType = fktOld.getMatchType().value();
        fkt.setMatchType(MatchTypeType.fromValue(sMatchType));
      } 
      if (fktOld.getDeleteAction() != null)
        fkt.setDeleteAction(ReferentialActionType.fromValue(fktOld.getDeleteAction())); 
      if (fktOld.getUpdateAction() != null)
        fkt.setUpdateAction(ReferentialActionType.fromValue(fktOld.getUpdateAction())); 
      fkt.setDescription(fktOld.getDescription());
    } 
    return fkt;
  }
  
  private static ForeignKeysType convertForeignKeys(ForeignKeysType fktsOld) {
    ForeignKeysType fkts = null;
    if (fktsOld != null) {
      
      fkts = _of.createForeignKeysType();
      for (int i = 0; i < fktsOld.getForeignKey().size(); i++) {
        
        ForeignKeyType fkt = convertForeignKey(fktsOld.getForeignKey().get(i));
        fkts.getForeignKey().add(fkt);
      } 
    } 
    return fkts;
  }
  
  private static UniqueKeyType convertCandidateKey(CandidateKeyType cktOld) {
    UniqueKeyType ukt = null;
    if (cktOld != null) {
      
      ukt = _of.createUniqueKeyType();
      ukt.setName(cktOld.getName());
      ukt.setDescription(cktOld.getDescription());
      for (int i = 0; i < cktOld.getColumn().size(); i++)
        ukt.getColumn().add(cktOld.getColumn().get(i)); 
    } 
    return ukt;
  }
  
  private static CandidateKeysType convertCandidateKeys(CandidateKeysType cktsOld) {
    CandidateKeysType ckts = null;
    if (cktsOld != null) {
      
      ckts = _of.createCandidateKeysType();
      for (int i = 0; i < cktsOld.getCandidateKey().size(); i++) {
        
        UniqueKeyType ukt = convertCandidateKey(cktsOld.getCandidateKey().get(i));
        ckts.getCandidateKey().add(ukt);
      } 
    } 
    return ckts;
  }
  
  private static CheckConstraintType convertCheckConstraint(CheckConstraintType cctOld) {
    CheckConstraintType cct = null;
    if (cctOld != null) {
      
      cct = _of.createCheckConstraintType();
      cct.setName(cctOld.getName());
      cct.setCondition(cctOld.getCondition());
      cct.setDescription(cctOld.getDescription());
    } 
    return cct;
  }
  
  private static CheckConstraintsType convertCheckConstraints(CheckConstraintsType cctsOld) {
    CheckConstraintsType ccts = null;
    if (cctsOld != null) {
      
      ccts = _of.createCheckConstraintsType();
      for (int i = 0; i < cctsOld.getCheckConstraint().size(); i++) {
        
        CheckConstraintType cct = convertCheckConstraint(cctsOld.getCheckConstraint().get(i));
        ccts.getCheckConstraint().add(cct);
      } 
    } 
    return ccts;
  }
  
  private static ActionTimeType convertActionTime(ActionTimeType attOld) {
    ActionTimeType att = null;
    if (attOld != null)
    {
      switch (attOld) {
        case ADMIN:
          att = ActionTimeType.BEFORE; break;
        case GRANT: att = ActionTimeType.AFTER;
          break;
      }  } 
    return att;
  }
  
  private static TriggerType convertTrigger(TriggerType ttOld) {
    TriggerType tt = null;
    if (ttOld != null) {
      
      tt = _of.createTriggerType();
      tt.setName(ttOld.getName());
      tt.setActionTime(convertActionTime(ttOld.getActionTime()));
      tt.setTriggerEvent(ttOld.getTriggerEvent());
      tt.setAliasList(ttOld.getAliasList());
      tt.setTriggeredAction(ttOld.getTriggeredAction());
      tt.setDescription(ttOld.getDescription());
    } 
    return tt;
  }
  
  private static TriggersType convertTriggers(TriggersType ttsOld) {
    TriggersType tts = null;
    if (ttsOld != null) {
      
      tts = _of.createTriggersType();
      for (int i = 0; i < ttsOld.getTrigger().size(); i++) {
        
        TriggerType tt = convertTrigger(ttsOld.getTrigger().get(i));
        tts.getTrigger().add(tt);
      } 
    } 
    return tts;
  }

  
  private static TableType convertTable(TableType ttOld) throws JAXBException {
    TableType tt = null;
    if (ttOld != null) {
      
      tt = _of.createTableType();
      tt.setName(ttOld.getName());
      tt.setFolder(ttOld.getFolder());
      tt.setDescription(ttOld.getDescription());
      tt.setColumns(convertColumns(ttOld.getColumns()));
      tt.setPrimaryKey(convertPrimaryKey(ttOld.getPrimaryKey()));
      tt.setForeignKeys(convertForeignKeys(ttOld.getForeignKeys()));
      tt.setCandidateKeys(convertCandidateKeys(ttOld.getCandidateKeys()));
      tt.setCheckConstraints(convertCheckConstraints(ttOld.getCheckConstraints()));
      tt.setTriggers(convertTriggers(ttOld.getTriggers()));
      tt.setRows(ttOld.getRows());
    } 
    return tt;
  }

  
  private static TablesType convertTables(TablesType ttsOld) throws JAXBException {
    TablesType tts = null;
    if (ttsOld != null) {
      
      tts = _of.createTablesType();
      for (int i = 0; i < ttsOld.getTable().size(); i++) {
        
        TableType tt = convertTable(ttsOld.getTable().get(i));
        tts.getTable().add(tt);
      } 
    } 
    return tts;
  }

  
  private static ViewType convertView(ViewType vtOld) throws JAXBException {
    ViewType vt = null;
    if (vtOld != null) {
      
      vt = _of.createViewType();
      vt.setName(vtOld.getName());
      vt.setQuery(vtOld.getQuery());
      vt.setQueryOriginal(vtOld.getQueryOriginal());
      vt.setDescription(vtOld.getDescription());
      vt.setColumns(convertColumns(vtOld.getColumns()));
    } 
    return vt;
  }

  
  private static ViewsType convertViews(ViewsType vtsOld) throws JAXBException {
    ViewsType vts = null;
    if (vtsOld != null) {
      
      vts = _of.createViewsType();
      for (int i = 0; i < vtsOld.getView().size(); i++) {
        
        ViewType vt = convertView(vtsOld.getView().get(i));
        vts.getView().add(vt);
      } 
    } 
    return vts;
  }
  
  private static ParameterType convertParameter(ParameterType ptOld) {
    ParameterType pt = null;
    if (ptOld != null) {
      
      pt = _of.createParameterType();
      pt.setName(ptOld.getName());
      pt.setMode(ptOld.getMode());
      pt.setType(ptOld.getType());
      pt.setTypeOriginal(ptOld.getTypeOriginal());
      pt.setDescription(ptOld.getDescription());
    } 
    return pt;
  }
  
  private static ParametersType convertParameters(ParametersType ptsOld) {
    ParametersType pts = null;
    if (ptsOld != null) {
      
      pts = _of.createParametersType();
      for (int i = 0; i < ptsOld.getParameter().size(); i++) {
        
        ParameterType pt = convertParameter(ptsOld.getParameter().get(i));
        pts.getParameter().add(pt);
      } 
    } 
    return pts;
  }
  
  private static RoutineType convertRoutine(RoutineType rtOld) {
    RoutineType rt = null;
    if (rtOld != null) {
      
      rt = _of.createRoutineType();
      rt.setSpecificName(rtOld.getName());
      rt.setName(rtOld.getName());
      rt.setSource(rtOld.getSource());
      rt.setBody(rtOld.getBody());
      rt.setCharacteristic(rtOld.getCharacteristic());
      rt.setReturnType(rtOld.getReturnType());
      rt.setParameters(convertParameters(rtOld.getParameters()));
    } 
    return rt;
  }
  
  private static RoutinesType convertRoutines(RoutinesType rtsOld) {
    RoutinesType rts = null;
    if (rtsOld != null) {
      
      rts = _of.createRoutinesType();
      for (int i = 0; i < rtsOld.getRoutine().size(); i++) {
        
        RoutineType rt = convertRoutine(rtsOld.getRoutine().get(i));
        rts.getRoutine().add(rt);
      } 
    } 
    return rts;
  }

  
  private static SchemaType convertSchema(SchemaType stOld) throws JAXBException {
    SchemaType st = null;
    if (stOld != null) {
      
      st = _of.createSchemaType();
      st.setName(stOld.getName());
      st.setFolder(stOld.getFolder());
      st.setDescription(stOld.getDescription());
      st.setTables(convertTables(stOld.getTables()));
      st.setViews(convertViews(stOld.getViews()));
      st.setRoutines(convertRoutines(stOld.getRoutines()));
    } 
    return st;
  }

  
  private static SchemasType convertSchemas(SchemasType stsOld) throws JAXBException {
    SchemasType sts = null;
    if (stsOld != null) {
      
      sts = _of.createSchemasType();
      for (int i = 0; i < stsOld.getSchema().size(); i++) {
        
        SchemaType st = convertSchema(stsOld.getSchema().get(i));
        sts.getSchema().add(st);
      } 
    } 
    return sts;
  }
  
  private static UserType convertUser(UserType utOld) {
    UserType ut = null;
    if (utOld != null) {
      
      ut = _of.createUserType();
      ut.setName(utOld.getName());
      ut.setDescription(utOld.getDescription());
    } 
    return ut;
  }
  
  private static UsersType convertUsers(UsersType utsOld) {
    UsersType uts = null;
    if (utsOld != null) {
      
      uts = _of.createUsersType();
      for (int i = 0; i < utsOld.getUser().size(); i++) {
        
        UserType ut = convertUser(utsOld.getUser().get(i));
        uts.getUser().add(ut);
      } 
    } 
    return uts;
  }
  
  private static RoleType convertRole(RoleType rtOld) {
    RoleType rt = null;
    if (rtOld != null) {
      
      rt = _of.createRoleType();
      rt.setName(rtOld.getName());
      rt.setAdmin(rtOld.getAdmin());
      rt.setDescription(rtOld.getDescription());
    } 
    return rt;
  }
  
  private static RolesType convertRoles(RolesType rtsOld) {
    RolesType rts = null;
    if (rtsOld != null) {
      
      rts = _of.createRolesType();
      for (int i = 0; i < rtsOld.getRole().size(); i++) {
        
        RoleType rt = convertRole(rtsOld.getRole().get(i));
        rts.getRole().add(rt);
      } 
    } 
    return rts;
  }
  
  private static PrivOptionType convertPrivOption(PrivOptionType potOld) {
    PrivOptionType pot = null;
    if (potOld != null)
    {
      switch (potOld) {
        case ADMIN:
          pot = PrivOptionType.ADMIN; break;
        case GRANT: pot = PrivOptionType.GRANT;
          break;
      }  } 
    return pot;
  }
  
  private static PrivilegeType convertPrivilege(PrivilegeType ptOld) {
    PrivilegeType pt = null;
    if (ptOld != null) {
      
      pt = _of.createPrivilegeType();
      pt.setType(ptOld.getType());
      pt.setObject(ptOld.getObject());
      pt.setGrantor(ptOld.getGrantor());
      pt.setGrantee(ptOld.getGrantee());
      pt.setOption(convertPrivOption(ptOld.getOption()));
      pt.setDescription(ptOld.getDescription());
    } 
    return pt;
  }
  
  private static PrivilegesType convertPrivileges(PrivilegesType ptsOld) {
    PrivilegesType pts = null;
    if (ptsOld != null) {
      
      pts = _of.createPrivilegesType();
      for (int i = 0; i < ptsOld.getPrivilege().size(); i++) {
        
        PrivilegeType pt = convertPrivilege(ptsOld.getPrivilege().get(i));
        pts.getPrivilege().add(pt);
      } 
    } 
    return pts;
  }
  
  private static MessageDigestType convertMessageDigest(String sMessageDigest) {
    MessageDigestType md = null;
    if (sMessageDigest != null && sMessageDigest.length() > 0) {
      
      String sDigestType = sMessageDigest.substring(0, 3);
      DigestTypeType dtt = DigestTypeType.fromValue(sDigestType);
      String sDigest = sMessageDigest.substring(3);
      md = _of.createMessageDigestType();
      md.setDigestType(dtt);
      md.setDigest(sDigest);
    } 
    return md;
  }

  
  private static SiardArchive convertArchive(SiardArchive saOld) throws JAXBException {
    SiardArchive sa = _of.createSiardArchive();
    sa.setVersion("2.2");
    sa.setDbname(saOld.getDbname());
    sa.setDescription(saOld.getDescription());
    sa.setArchiver(saOld.getArchiver());
    sa.setArchiverContact(saOld.getArchiverContact());
    sa.setDataOwner(saOld.getDataOwner());
    sa.setDataOriginTimespan(saOld.getDataOriginTimespan());
    
    sa.setProducerApplication(saOld.getProducerApplication());
    sa.setArchivalDate(saOld.getArchivalDate());
    MessageDigestType mdt = convertMessageDigest(saOld.getMessageDigest());
    if (mdt != null)
      sa.getMessageDigest().add(mdt); 
    sa.setClientMachine(saOld.getClientMachine());
    sa.setDatabaseProduct(saOld.getDatabaseProduct());
    sa.setConnection(saOld.getConnection());
    sa.setDatabaseUser(saOld.getDatabaseUser());
    sa.setSchemas(convertSchemas(saOld.getSchemas()));
    sa.setUsers(convertUsers(saOld.getUsers()));
    sa.setRoles(convertRoles(saOld.getRoles()));
    sa.setPrivileges(convertPrivileges(saOld.getPrivileges()));
    return sa;
  }







  
  public static SiardArchive readSiard22Xml(InputStream isXml) {
    SiardArchive sa = null;
    
    try {
      URL urlXsd = Archive.class.getResource("/ch/admin/bar/siard2/api/res/metadata.xsd");
      sa = (SiardArchive)Io.readJaxbObject(SiardArchive.class, isXml, urlXsd);
    } catch (JAXBException je) {
      _il.exception(je);
      System.err.println(EU.getExceptionMessage(je));
    } 
    return sa;
  }
  
  public static SiardArchive readSiard21Xml(InputStream fileInputStream) {
    SiardArchive sa = null;
    SiardArchive siardArchive = null;
    try {
      URL urlXsd = Archive.class.getResource("/ch/admin/bar/siard2/api/res/old21/metadata.xsd");
      sa = (SiardArchive)Io.readJaxbObject(ConvertableSiard21Archive.class, fileInputStream, urlXsd);
      siardArchive = ((ConvertableSiard21Archive)sa).accept(new Siard21ToSiard22Transformer());
    }
    catch (JAXBException je) {
      _il.exception(je);
      System.err.println(EU.getExceptionMessage(je));
    } 
    return siardArchive;
  }








  
  public static SiardArchive readAndConvertSiard10Xml(InputStream isXml) {
    SiardArchive sa = null;
    
    try {
      URL urlXsd = Archive.class.getResource("/ch/admin/bar/siard2/api/res/old10/metadata.xsd");
      
      SiardArchive saOld10 = (SiardArchive)Io.readJaxbObject(SiardArchive.class, isXml, urlXsd);
      if (saOld10 != null)
        sa = convertArchive(saOld10); 
    } catch (JAXBException je) {
      _il.exception(je);
      System.err.println(EU.getExceptionMessage(je));
    } 
    return sa;
  }










  
  static void writeXml(SiardArchive sa, OutputStream osXml, boolean bValidate) throws JAXBException {
    if (bValidate) {
      
      URL urlXsd = Archive.class.getResource("/ch/admin/bar/siard2/api/res/metadata.xsd");
      String sSchemaLocation = "http://www.bar.admin.ch/xmlns/siard/2/metadata.xsd";
      int i = sSchemaLocation.lastIndexOf('/');
      if (i >= 0)
        sSchemaLocation = sSchemaLocation + " " + sSchemaLocation.substring(i + 1); 
      Io.writeJaxbObject(sa, osXml, sSchemaLocation, true, urlXsd);
    } else {
      
      Io.writeJaxbObject(sa, osXml, null, null, true);
    } 
  }
}


/* Location:              C:\Users\lenovo\IdeaProjects\SIARD_KR_2.2\lib\siardapi.jar!\ch\admin\bar\siard2\api\primary\MetaDataXml.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */