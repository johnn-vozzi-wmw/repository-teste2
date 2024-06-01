package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.DapLaudoAtua;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class DapLaudoAtuaDbxDao extends LavendereCrudDbxDao  {
	
	private static DapLaudoAtuaDbxDao instance;
	
	public static DapLaudoAtuaDbxDao getInstance() {
		if (instance == null) {
			instance = new DapLaudoAtuaDbxDao();
		}
		return instance;
	}

	public DapLaudoAtuaDbxDao() {
		super(DapLaudoAtua.TABLE_NAME);
	}

	@Override
	protected BaseDomain populate(BaseDomain domain, ResultSet rs) throws SQLException {
		DapLaudoAtua dapLaudoAtua = new DapLaudoAtua();
		dapLaudoAtua.rowKey = rs.getString("rowKey");
		dapLaudoAtua.cdEmpresa  = rs.getString("cdEmpresa");
		dapLaudoAtua.cdCliente = rs.getString("cdCliente");
		dapLaudoAtua.cdDapMatricula = rs.getString("cdDapMatricula");
		dapLaudoAtua.cdDapCultura = rs.getString("cdDapCultura");
		dapLaudoAtua.cdDapLaudo = rs.getString("cdDapLaudo");
		dapLaudoAtua.cdDapLaudoAtua = rs.getString("cdDapLaudoAtua");
		dapLaudoAtua.cdSafra = rs.getString("cdSafra");
		dapLaudoAtua.dtEmissao = rs.getDate("dtEmissao");
		dapLaudoAtua.nuSeqLaudo = rs.getInt("cdDapCultura");
		dapLaudoAtua.qtArea = rs.getDouble("qtArea");
		dapLaudoAtua.dsAspectoCultura = rs.getString("dsAspectoCultura");
		dapLaudoAtua.dsRecomendacoes = rs.getString("dsRecomendacoes");
		dapLaudoAtua.nmImgAssTecnico = rs.getString("nmImgAssTecnico");
		dapLaudoAtua.nmImgAssCliente = rs.getString("nmImgAssCliente");
		dapLaudoAtua.cdLatitude = rs.getDouble("cdLatitude");
		dapLaudoAtua.cdLongitude = rs.getDouble("cdLongitude");
		dapLaudoAtua.dsObservacao = rs.getString("dsObservacao");
		dapLaudoAtua.flTipoAlteracao = rs.getString("flTipoAlteracao");
		dapLaudoAtua.flStatusLaudo = rs.getString("flStatusLaudo");
		return dapLaudoAtua;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowKey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDCLIENTE,");
		sql.append(" CDSAFRA,");
		sql.append(" CDDAPMATRICULA,");
		sql.append(" CDDAPCULTURA,");
		sql.append(" CDDAPLAUDO,");
		sql.append(" CDDAPLAUDOATUA,");
		sql.append(" NUSEQLAUDO,");
		sql.append(" DSASPECTOCULTURA,");
		sql.append(" DSRECOMENDACOES,");
		sql.append(" NMIMGASSTECNICO,");
		sql.append(" NMIMGASSCLIENTE,");
		sql.append(" FLSTATUSLAUDO,");
		sql.append(" QTAREA,");
		sql.append(" DTEMISSAO,");
		sql.append(" CDLATITUDE,");
		sql.append(" CDLONGITUDE,");
		sql.append(" DSOBSERVACAO,");
		sql.append(" FLTIPOALTERACAO");
	}
	
	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DapLaudoAtua();
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		DapLaudoAtua dapLaudoAtua = (DapLaudoAtua) domain;
		sql.append(" DSASPECTOCULTURA = ").append(Sql.getValue(dapLaudoAtua.dsAspectoCultura)).append(",");
		sql.append(" DSRECOMENDACOES = ").append(Sql.getValue(dapLaudoAtua.dsRecomendacoes)).append(",");
		sql.append(" NMIMGASSTECNICO = ").append(Sql.getValue(dapLaudoAtua.nmImgAssTecnico)).append(",");
		sql.append(" NMIMGASSCLIENTE = ").append(Sql.getValue(dapLaudoAtua.nmImgAssCliente)).append(",");
		sql.append(" FLSTATUSLAUDO = ").append(Sql.getValue(dapLaudoAtua.flStatusLaudo)).append(",");
		sql.append(" QTAREA = ").append(Sql.getValue(dapLaudoAtua.qtArea)).append(",");
		sql.append(" DTEMISSAO = ").append(dapLaudoAtua.dtEmissao).append(",");
		sql.append(" CDLATITUDE = ").append(Sql.getValue(dapLaudoAtua.cdLatitude)).append(",");
		sql.append(" CDLONGITUDE = ").append(Sql.getValue(dapLaudoAtua.cdLongitude)).append(",");
		sql.append(" DSOBSERVACAO = ").append(Sql.getValue(dapLaudoAtua.dsObservacao));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		DapLaudoAtua dapLaudoAtua = (DapLaudoAtua) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		addWhere(dapLaudoAtua, sqlWhereClause);
		if (dapLaudoAtua.somenteLaudosNaoEnviados) {
			sqlWhereClause.addAndConditionNotEquals(" FLTIPOALTERACAO ", BaseDomain.FLTIPOALTERACAO_ORIGINAL);
		}
		sql.append(sqlWhereClause.getSql());
	}

	private void addWhere(DapLaudoAtua dapLaudoAtua, SqlWhereClause sqlWhereClause) {
		sqlWhereClause.addAndConditionEquals(" CDEMPRESA ", dapLaudoAtua.cdEmpresa);
		sqlWhereClause.addAndConditionEquals(" CDCLIENTE ", dapLaudoAtua.cdCliente);
		sqlWhereClause.addAndConditionEquals(" CDSAFRA ", dapLaudoAtua.cdSafra);
		sqlWhereClause.addAndConditionEquals(" CDDAPMATRICULA ", dapLaudoAtua.cdDapMatricula);
		sqlWhereClause.addAndConditionEquals(" CDDAPCULTURA ", dapLaudoAtua.cdDapCultura);
		sqlWhereClause.addAndConditionEquals(" CDDAPLAUDO ", dapLaudoAtua.cdDapLaudo);
		sqlWhereClause.addAndConditionEquals(" CDDAPLAUDOATUA ", dapLaudoAtua.cdDapLaudoAtua);
	}

	@Override
	protected void addWhereDeleteByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		DapLaudoAtua dapLaudoAtua = (DapLaudoAtua) domain;
		addWhere(dapLaudoAtua, sqlWhereClause);
		if (dapLaudoAtua.isDeleteLaudosAtuaNaoEnviados) {
			sqlWhereClause.addAndConditionNotEquals(" FLTIPOALTERACAO ", BaseDomain.FLTIPOALTERACAO_ORIGINAL);
			sql.append(sqlWhereClause.getSql());
		}
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,");
		sql.append(" CDCLIENTE,");
		sql.append(" CDSAFRA,");
		sql.append(" CDDAPMATRICULA,");
		sql.append(" CDDAPCULTURA,");
		sql.append(" CDDAPLAUDO,");
		sql.append(" CDDAPLAUDOATUA,");
		sql.append(" NUSEQLAUDO,");
		sql.append(" DSASPECTOCULTURA,");
		sql.append(" DSRECOMENDACOES,");
		sql.append(" NMIMGASSTECNICO,");
		sql.append(" NMIMGASSCLIENTE,");
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
		DapLaudoAtua dapLaudoAtua = (DapLaudoAtua) domain; 
    	sql.append(Sql.getValue(dapLaudoAtua.cdEmpresa)).append(",");
        sql.append(Sql.getValue(dapLaudoAtua.cdCliente)).append(",");
        sql.append(Sql.getValue(dapLaudoAtua.cdSafra)).append(",");
        sql.append(Sql.getValue(dapLaudoAtua.cdDapMatricula)).append(",");
        sql.append(Sql.getValue(dapLaudoAtua.cdDapCultura)).append(",");
        sql.append(Sql.getValue(dapLaudoAtua.cdDapLaudo)).append(",");
        sql.append(Sql.getValue(dapLaudoAtua.cdDapLaudoAtua)).append(",");
        sql.append(Sql.getValue(dapLaudoAtua.nuSeqLaudo)).append(",");
        sql.append(Sql.getValue(dapLaudoAtua.dsAspectoCultura)).append(",");
        sql.append(Sql.getValue(dapLaudoAtua.dsRecomendacoes)).append(",");
        sql.append(Sql.getValue(dapLaudoAtua.nmImgAssTecnico)).append(",");
        sql.append(Sql.getValue(dapLaudoAtua.nmImgAssCliente)).append(",");
        sql.append(Sql.getValue(dapLaudoAtua.flStatusLaudo)).append(",");
        sql.append(Sql.getValue(dapLaudoAtua.qtArea)).append(",");
        sql.append(Sql.getValue(dapLaudoAtua.dtEmissao)).append(",");
        sql.append(Sql.getValue(dapLaudoAtua.cdLatitude)).append(",");
        sql.append(Sql.getValue(dapLaudoAtua.cdLongitude)).append(",");
        sql.append(Sql.getValue(dapLaudoAtua.dsObservacao)).append(",");
        sql.append(Sql.getValue(dapLaudoAtua.nuCarimbo)).append(",");
        sql.append(Sql.getValue(dapLaudoAtua.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(dapLaudoAtua.cdUsuario));
	}

	public static String getSqlDadosEnvioServidor() {
    	StringBuffer sql = new StringBuffer();
		sql.append("select * from (");
    	sql.append("SELECT tb.* FROM TBLVPDAPLAUDOATUA tb ");
    	sql.append(" WHERE (tb.FLSTATUSLAUDO = 'F' OR tb.FLSTATUSLAUDO = 'E')");
    	sql.append(" AND tb.").append(DapLaudoAtua.NMCAMPOTIPOALTERACAO);
    	sql.append(" <> ").append(Sql.getValue(DapLaudoAtua.FLTIPOALTERACAO_ORIGINAL));
		sql.append(" order by CDEMPRESA, CDCLIENTE, CDDAPMATRICULA, CDSAFRA, CDDAPCULTURA, CDDAPLAUDO, CAST(tb.CDDAPLAUDOATUA as decimal) desc ");
		sql.append(") group by CDEMPRESA, CDCLIENTE, CDDAPMATRICULA, CDSAFRA, CDDAPCULTURA, CDDAPLAUDO;");
    	return sql.toString();
	}
	
	@Override
	public Vector findAllAlterados() throws SQLException {
		BaseDomain domainFilter = getBaseDomainDefault();
		StringBuffer sql = getSqlBuffer();
		sql.append("select * from (");
		sql.append(" select ");
		addSelectColumns(domainFilter, sql);
		sql.append(" from ");
		sql.append(tableName);
		sql.append(" tb ");
		addJoin(domainFilter, sql);
		sql.append(" where fltipoalteracao != ");
		sql.append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ORIGINAL));
		sql.append(" and tb.FLSTATUSLAUDO = 'F' order by CDEMPRESA, CDCLIENTE, CDDAPMATRICULA, CDSAFRA, CDDAPCULTURA, CDDAPLAUDO, CAST(tb.CDDAPLAUDOATUA as decimal) asc ");
		sql.append(") group by CDEMPRESA, CDCLIENTE, CDDAPMATRICULA, CDSAFRA, CDDAPCULTURA, CDDAPLAUDO;");
		return findAll(domainFilter, sql.toString());
	}

}
