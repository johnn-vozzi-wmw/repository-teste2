package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TermoCorrecao;
import totalcross.sql.ResultSet;

public class TermoCorrecaoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TermoCorrecao();
	}

    private static TermoCorrecaoDbxDao instance;

    public TermoCorrecaoDbxDao() {
        super(TermoCorrecao.TABLE_NAME); 
    }
    
    public static TermoCorrecaoDbxDao getInstance() {
        if (instance == null) {
            instance = new TermoCorrecaoDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        TermoCorrecao termocorrecao = new TermoCorrecao();
        termocorrecao.rowKey = rs.getString("rowkey");
        termocorrecao.dsTermo = rs.getString("dsTermo");
        termocorrecao.dsTermocorrigido = rs.getString("dsTermocorrigido");
        termocorrecao.cdUsuario = rs.getString("cdUsuario");
        termocorrecao.nuCarimbo = rs.getInt("nuCarimbo");
        termocorrecao.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return termocorrecao;
    }
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" DSTERMO,");
        sql.append(" DSTERMOCORRIGIDO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" DSTERMO,");
        sql.append(" DSTERMOCORRIGIDO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        TermoCorrecao termocorrecao = (TermoCorrecao) domain;
        sql.append(Sql.getValue(termocorrecao.dsTermo)).append(",");
        sql.append(Sql.getValue(termocorrecao.dsTermocorrigido)).append(",");
        sql.append(Sql.getValue(termocorrecao.cdUsuario)).append(",");
        sql.append(Sql.getValue(termocorrecao.nuCarimbo)).append(",");
        sql.append(Sql.getValue(termocorrecao.flTipoAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        TermoCorrecao termocorrecao = (TermoCorrecao) domain;
        sql.append(" CDUSUARIO = ").append(Sql.getValue(termocorrecao.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(termocorrecao.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(termocorrecao.flTipoAlteracao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        TermoCorrecao termocorrecao = (TermoCorrecao) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("DSTERMO = ", termocorrecao.dsTermo);
		sqlWhereClause.addAndCondition("DSTERMOCORRIGIDO = ", termocorrecao.dsTermocorrigido);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}