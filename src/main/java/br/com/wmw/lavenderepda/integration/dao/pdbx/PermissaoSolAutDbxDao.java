package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.PermissaoSolAut;
import br.com.wmw.lavenderepda.business.enums.TipoSolicitacaoAutorizacaoEnum;
import totalcross.sql.ResultSet;

public class PermissaoSolAutDbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PermissaoSolAut();
	}

    private static PermissaoSolAutDbxDao instance;

    public PermissaoSolAutDbxDao() { super(PermissaoSolAut.TABLE_NAME); }

    public static PermissaoSolAutDbxDao getInstance() { return (instance == null) ? instance = new PermissaoSolAutDbxDao() : instance; }
	@Override protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { addSelectColumns(null, sql); }
	@Override protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException { return populate(domainFilter, rs); }
	@Override protected String getInsertValue(String columnName, int columnType, int columnSize, BaseDomain domain) { return null; }
    @Override protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {}
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
	    PermissaoSolAut permissaoSolAut = new PermissaoSolAut();
	    permissaoSolAut.rowKey = rs.getString("rowkey");
	    permissaoSolAut.cdEmpresa = rs.getString("cdEmpresa");
	    permissaoSolAut.cdUsuarioPermissao = rs.getString("cdUsuarioPermissao");
	    permissaoSolAut.tipoSolicitacaoAutorizacaoEnum = TipoSolicitacaoAutorizacaoEnum.values()[rs.getInt("cdTipoSolAutorizacao")];
	    permissaoSolAut.cdSistema  = rs.getInt("cdSistema");
	    permissaoSolAut.flSomenteLeitura  = rs.getString("flSomenteLeitura");
	    permissaoSolAut.flIgnoraUsuarioGrupoProd  = rs.getString("flIgnoraUsuarioGrupoProd");
	    permissaoSolAut.cdUsuario = rs.getString("cdUsuario");
        return permissaoSolAut;
    }

	@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
	    sql.append(" tb.rowkey,");
	    sql.append(" tb.CDEMPRESA,");
	    sql.append(" tb.CDUSUARIOPERMISSAO,");
	    sql.append(" tb.CDTIPOSOLAUTORIZACAO,");
		sql.append(" tb.CDSISTEMA,");
		sql.append(" tb.FLSOMENTELEITURA,");
		sql.append(" tb.FLIGNORAUSUARIOGRUPOPROD,");
	    sql.append(" tb.CDUSUARIO");
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        PermissaoSolAut permissaoSolAut = (PermissaoSolAut) domain;
	    SqlWhereClause sqlWhereClause = new SqlWhereClause();
	    sqlWhereClause.addAndCondition("CDEMPRESA = ", permissaoSolAut.cdEmpresa);
	    sqlWhereClause.addAndCondition("CDUSUARIOPERMISSAO = ", permissaoSolAut.cdUsuarioPermissao);
	    if (permissaoSolAut.tipoSolicitacaoAutorizacaoEnum != null) {
		    sqlWhereClause.addAndCondition("tb.CDTIPOSOLAUTORIZACAO = ", permissaoSolAut.tipoSolicitacaoAutorizacaoEnum.ordinal());
	    }
	    sqlWhereClause.addAndCondition("CDSISTEMA = ", permissaoSolAut.cdSistema);
    }

}