package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.DapCultura;
import br.com.wmw.lavenderepda.business.domain.DapLaudo;
import totalcross.sql.PreparedStatement;
import totalcross.sql.ResultSet;

public class DapLaudoPdbxDao extends CrudDbxDao {
	
	private static DapLaudoPdbxDao instance;
	
	public static DapLaudoPdbxDao getInstance() {
		if (instance == null) {
			instance = new DapLaudoPdbxDao();
		}
		return instance;
	}

	public DapLaudoPdbxDao() {
		super(DapLaudo.TABLE_NAME);
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		DapLaudo dapLaudo = new DapLaudo();
		dapLaudo.rowKey = rs.getString("rowKey");
		dapLaudo.cdEmpresa = rs.getString("cdEmpresa");
		dapLaudo.cdCliente = rs.getString("cdCliente");
		dapLaudo.cdDapMatricula = rs.getString("cdDapMatricula");
		dapLaudo.cdDapCultura = rs.getString("cdDapCultura");
		dapLaudo.cdDapLaudo = rs.getString("cdDapLaudo");
		dapLaudo.nmRazaoSocial = rs.getString("nmRazaoSocial");
		dapLaudo.nuCnpj = rs.getString("nuCnpj");
		dapLaudo.cdSafra = rs.getString("cdSafra");
		dapLaudo.dtEmissao = rs.getDate("dtEmissao");
		dapLaudo.nuSeqLaudo = rs.getInt("nuSeqLaudo");
		dapLaudo.qtArea = rs.getDouble("qtArea");
		dapLaudo.dsAspectoCultura = rs.getString("dsAspectoCultura");
		dapLaudo.dsRecomendacoes = rs.getString("dsRecomendacoes");
		dapLaudo.imAssTecnico = rs.getBytes("imAssTecnico");
		dapLaudo.imAssCliente = rs.getBytes("imAssCliente");
		dapLaudo.dsCidade = rs.getString("dsCidade");
		dapLaudo.dsLocalidade = rs.getString("dsLocalidade");
		dapLaudo.cdUf = rs.getString("cdUf");
		dapLaudo.dsSafra = rs.getString("dsSafra");
		dapLaudo.cdLatitude = rs.getDouble("cdLatitude");
		dapLaudo.cdLongitude = rs.getDouble("cdLongitude");
		dapLaudo.dsObservacao = rs.getString("dsObservacao");
		dapLaudo.flStatusLaudo = rs.getString("flStatusLaudo");
		dapLaudo.flTipoAlteracao = rs.getString("flTipoAlteracao");
		populateDapClienteCultura(dapLaudo, rs);
		return dapLaudo;
	}

	private void populateDapClienteCultura(DapLaudo dapLaudo, ResultSet rs) throws SQLException {
		DapCultura dapCultura = new DapCultura();
		dapCultura.cdEmpresa = dapLaudo.cdEmpresa;
		dapCultura.cdCliente = dapLaudo.cdCliente;
		dapCultura.cdDapMatricula = dapLaudo.cdDapMatricula;
		dapCultura.cdDapCultura = dapLaudo.cdDapCultura;
		dapCultura.dsDapCultura = rs.getString("dsDapCultura");
		dapCultura.qtArea = rs.getDouble("qtAreaCultura");
		dapLaudo.dapCultura = dapCultura;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowKey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDCLIENTE,");
		sql.append(" tb.CDSAFRA,");
		sql.append(" tb.CDDAPMATRICULA,");
		sql.append(" tb.CDDAPCULTURA,");
		sql.append(" tb.CDDAPLAUDO,");
		sql.append(" tb.NUSEQLAUDO,");
		sql.append(" tb.DSASPECTOCULTURA,");
		sql.append(" tb.DSRECOMENDACOES,");
		sql.append(" tb.IMASSTECNICO,");
		sql.append(" tb.IMASSCLIENTE,");
		sql.append(" tb.QTAREA,");
		sql.append(" tb.DTEMISSAO,");
		sql.append(" tb.CDLATITUDE,");
		sql.append(" tb.CDLONGITUDE,");
		sql.append(" tb.FLSTATUSLAUDO,");
		sql.append(" tb.FLTIPOALTERACAO,");
		sql.append(" tb.DSOBSERVACAO,");
		sql.append(" cli.NMRAZAOSOCIAL,");
		sql.append(" cli.NUCNPJ,");
		sql.append(" dapmat.DSCIDADE,");
		sql.append(" dapmat.DSLOCALIDADE,");
		sql.append(" dapmat.CDUF,");
		sql.append(" saf.DSSAFRA,");
		sql.append(" dapcult.DSDAPCULTURA,");
		sql.append(" dapcult.QTAREA AS QTAREACULTURA");
	}
	
	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		sql.append(" JOIN TBLVPDAPCULTURA dapcult ON");
		sql.append(" tb.CDEMPRESA = dapcult.CDEMPRESA");
		sql.append(" and tb.CDCLIENTE = dapcult.CDCLIENTE");
		sql.append(" and tb.CDSAFRA = dapcult.CDSAFRA");
		sql.append(" and tb.CDDAPCULTURA = dapcult.CDDAPCULTURA");
		sql.append(" and tb.CDDAPMATRICULA = dapcult.CDDAPMATRICULA");
		sql.append(" and tb.CDUSUARIO = dapcult.CDUSUARIO");
		sql.append(" JOIN TBLVPCLIENTE cli ON");
		sql.append(" tb.CDEMPRESA = cli.CDEMPRESA");
		sql.append(" and cli.CDREPRESENTANTE = ").append(SessionLavenderePda.getRepresentante().cdRepresentante);
		sql.append(" and tb.CDCLIENTE = cli.CDCLIENTE");
		sql.append(" and tb.CDUSUARIO = cli.CDUSUARIO");
		sql.append(" JOIN TBLVPDAPMATRICULA dapmat ON");
		sql.append(" tb.CDEMPRESA = dapmat.CDEMPRESA");
		sql.append(" and tb.CDCLIENTE = dapmat.CDCLIENTE");
		sql.append(" and tb.CDSAFRA = dapmat.CDSAFRA");
		sql.append(" and tb.CDDAPMATRICULA = dapmat.CDDAPMATRICULA");
		sql.append(" and tb.CDUSUARIO = dapmat.CDUSUARIO");
		sql.append(" JOIN TBLVPSAFRA saf ON");
		sql.append(" tb.CDSAFRA = saf.CDSAFRA");
		sql.append(" and tb.CDUSUARIO = saf.CDUSUARIO");
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DapLaudo();
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		DapLaudo dapLaudo = (DapLaudo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals(" tb.CDEMPRESA ", dapLaudo.cdEmpresa);
		sqlWhereClause.addAndConditionEquals(" tb.CDCLIENTE ", dapLaudo.cdCliente);
		sqlWhereClause.addAndConditionEquals(" tb.CDDAPCULTURA ", dapLaudo.cdDapCultura);
		sqlWhereClause.addAndConditionEquals(" tb.CDDAPMATRICULA ", dapLaudo.cdDapMatricula);
		sqlWhereClause.addAndConditionEquals(" tb.CDSAFRA ", dapLaudo.cdSafra);
		sqlWhereClause.addAndConditionEquals(" tb.FLSTATUSLAUDO ", dapLaudo.flStatusLaudo);
		sql.append(sqlWhereClause.getSql());
	}
	
	@Override
	protected void addWhereMaxKey(BaseDomain domain, StringBuffer sql) throws SQLException {
		DapLaudo dapLaudo = (DapLaudo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals(" CDEMPRESA ", dapLaudo.cdEmpresa);
		sqlWhereClause.addAndConditionEquals(" CDCLIENTE ", dapLaudo.cdCliente);
		sqlWhereClause.addAndConditionEquals(" CDDAPMATRICULA ", dapLaudo.cdDapMatricula);
		sqlWhereClause.addAndConditionEquals(" CDSAFRA ", dapLaudo.cdSafra);
		sqlWhereClause.addAndConditionEquals(" CDDAPCULTURA ", dapLaudo.cdDapCultura);
		sql.append(sqlWhereClause.getSql());
	}
	
	@Override
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		if ((domain != null) && !ValueUtil.isEmpty(domain.sortAtributte)) {
    		sql.append(" order by ");
    		String[] sortAtributtes = StringUtil.split(domain.sortAtributte, ',');
    		String[] sortAsc = StringUtil.split(domain.sortAsc, ',');
    		for (int i = 0; i < sortAtributtes.length; i++) {
    			sql.append(sortAtributtes[i]).append(ValueUtil.VALOR_SIM.equals(sortAsc[i]) ? " ASC" : " DESC");
    			if (!(i == (sortAtributtes.length - 1))) {
    				sql.append(" , ");
    			}
			}
    	}
	}
	
	protected void addWhereForUpdate(BaseDomain domain, StringBuffer sql) throws SQLException {
		DapLaudo dapLaudo = (DapLaudo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals(" CDEMPRESA ", dapLaudo.cdEmpresa);
		sqlWhereClause.addAndConditionEquals(" CDCLIENTE ", dapLaudo.cdCliente);
		sqlWhereClause.addAndConditionEquals(" CDSAFRA ", dapLaudo.cdSafra);
		sqlWhereClause.addAndConditionEquals(" CDDAPMATRICULA ", dapLaudo.cdDapMatricula);
		sqlWhereClause.addAndConditionEquals(" CDDAPCULTURA ", dapLaudo.cdDapCultura);
		sqlWhereClause.addAndConditionEquals(" CDDAPLAUDO ", dapLaudo.cdDapLaudo);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,");
		sql.append(" CDCLIENTE,");
		sql.append(" CDSAFRA,");
		sql.append(" CDDAPMATRICULA,");
		sql.append(" CDDAPCULTURA,");
		sql.append(" CDDAPLAUDO,");
		sql.append(" NUSEQLAUDO,");
		sql.append(" DSASPECTOCULTURA,");
		sql.append(" DSRECOMENDACOES,");
		sql.append(" IMASSTECNICO,");
		sql.append(" IMASSCLIENTE,");
		sql.append(" FLSTATUSLAUDO,");
		sql.append(" QTAREA,");
		sql.append(" DTEMISSAO,");
		sql.append(" CDLATITUDE,");
		sql.append(" CDLONGITUDE,");
		sql.append(" DSOBSERVACAO,");
		sql.append(" NUCARIMBO,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" CDUSUARIO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}
	
	@Override
	public void insert(BaseDomain domain) throws SQLException {
		DapLaudo dapLaudo = (DapLaudo) domain;
		StringBuffer sql = getSqlBuffer();
		sql.append("insert into ").append(DapLaudo.TABLE_NAME).append("(");
		addInsertColumns(sql);
		sql.append(",rowkey) values (");
		sql.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		try (PreparedStatement prStatement = prepareStatement(sql.toString())) {
			prStatement.setString(1, dapLaudo.cdEmpresa);
			prStatement.setString(2, dapLaudo.cdCliente);
			prStatement.setString(3, dapLaudo.cdSafra);
			prStatement.setString(4, dapLaudo.cdDapMatricula);
			prStatement.setString(5, dapLaudo.cdDapCultura);
			prStatement.setString(6, dapLaudo.cdDapLaudo);
			prStatement.setInt(7, dapLaudo.nuSeqLaudo);
			prStatement.setString(8, dapLaudo.dsAspectoCultura);
			prStatement.setString(9, dapLaudo.dsRecomendacoes);
			prStatement.setBytes(10, dapLaudo.imAssTecnico);
			prStatement.setBytes(11, dapLaudo.imAssCliente);
			prStatement.setString(12, dapLaudo.flStatusLaudo);
			prStatement.setDouble(13, dapLaudo.qtArea);
			prStatement.setDate(14, dapLaudo.dtEmissao);
			prStatement.setDouble(15, dapLaudo.cdLatitude);
			prStatement.setDouble(16, dapLaudo.cdLongitude);
			prStatement.setString(17, dapLaudo.dsObservacao);
			prStatement.setInt(18, dapLaudo.nuCarimbo);
			prStatement.setString(19, dapLaudo.flTipoAlteracao);
			prStatement.setString(20, dapLaudo.cdUsuario);
			prStatement.setString(21, dapLaudo.getRowKey());
			prStatement.executeQuery();
		}
	}
	
	@Override
	public void update(BaseDomain domain) throws SQLException {
		DapLaudo dapLaudo = (DapLaudo) domain;
		StringBuffer sql = getSqlBuffer();
		sql.append("update ").append(tableName).append(" SET ");
		sql.append(" DSASPECTOCULTURA = ?,");
		sql.append(" DSRECOMENDACOES = ?,");
		sql.append(" IMASSTECNICO = ?,");
		sql.append(" IMASSCLIENTE = ?,");
		sql.append(" FLSTATUSLAUDO = ?,");
		sql.append(" QTAREA = ?,");
		sql.append(" DTEMISSAO = ?,");
		sql.append(" CDLATITUDE = ?,");
		sql.append(" CDLONGITUDE = ?,");
		sql.append(" DSOBSERVACAO = ?,");
		sql.append(" flTipoAlteracao = ?");
		addWhereForUpdate(domain, sql);
		try (PreparedStatement prStatement = prepareStatement(sql.toString())) {
			prStatement.setString(1, dapLaudo.dsAspectoCultura);
			prStatement.setString(2, dapLaudo.dsRecomendacoes);
			prStatement.setBytes(3, dapLaudo.imAssTecnico);
			prStatement.setBytes(4, dapLaudo.imAssCliente);
			prStatement.setString(5, dapLaudo.flStatusLaudo);
			prStatement.setDouble(6, dapLaudo.qtArea);
			prStatement.setDate(7, dapLaudo.dtEmissao);
			prStatement.setDouble(8, dapLaudo.cdLatitude);
			prStatement.setDouble(9, dapLaudo.cdLongitude);
			prStatement.setString(10, dapLaudo.dsObservacao);
			prStatement.setString(11, dapLaudo.flTipoAlteracao);
			prStatement.executeUpdate();
		}
	}

}
