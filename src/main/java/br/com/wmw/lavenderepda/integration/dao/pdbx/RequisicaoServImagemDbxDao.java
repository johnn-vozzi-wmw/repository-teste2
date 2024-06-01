package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServImagem;
import totalcross.sql.ResultSet;

public class RequisicaoServImagemDbxDao extends LavendereCrudDbxDao {

    private static RequisicaoServImagemDbxDao instance;

    public RequisicaoServImagemDbxDao() {
        super(RequisicaoServImagem.TABLE_NAME); 
    }
    
    public static RequisicaoServImagemDbxDao getInstance() {
        if (instance == null) {
            instance = new RequisicaoServImagemDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        RequisicaoServImagem requisicaoServImagem = new RequisicaoServImagem();
        requisicaoServImagem.rowKey = rs.getString("rowkey");
        requisicaoServImagem.cdEmpresa = rs.getString("cdEmpresa");
        requisicaoServImagem.cdRepresentante = rs.getString("cdRepresentante");
        requisicaoServImagem.cdRequisicaoServ = rs.getString("cdRequisicaoServ");
        requisicaoServImagem.nmImagem = rs.getString("nmImagem");
        requisicaoServImagem.nuCarimbo = rs.getInt("nuCarimbo");
        requisicaoServImagem.flTipoAlteracao = rs.getString("flTipoAlteracao");
        requisicaoServImagem.cdUsuario = rs.getString("cdUsuario");
        return requisicaoServImagem;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDREQUISICAOSERV,");
        sql.append(" NMIMAGEM,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDREQUISICAOSERV,");
        sql.append(" NMIMAGEM,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        RequisicaoServImagem requisicaoServImagem = (RequisicaoServImagem) domain;
        sql.append(Sql.getValue(requisicaoServImagem.cdEmpresa)).append(",");
        sql.append(Sql.getValue(requisicaoServImagem.cdRepresentante)).append(",");
        sql.append(Sql.getValue(requisicaoServImagem.cdRequisicaoServ)).append(",");
        sql.append(Sql.getValue(requisicaoServImagem.nmImagem)).append(",");
        sql.append(Sql.getValue(requisicaoServImagem.nuCarimbo)).append(",");
        sql.append(Sql.getValue(requisicaoServImagem.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(requisicaoServImagem.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        RequisicaoServImagem requisicaoServImagem = (RequisicaoServImagem) domain;
        sql.append(" NUCARIMBO = ").append(Sql.getValue(requisicaoServImagem.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(requisicaoServImagem.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(requisicaoServImagem.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        RequisicaoServImagem requisicaoServImagem = (RequisicaoServImagem) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", requisicaoServImagem.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", requisicaoServImagem.cdRepresentante);
		sqlWhereClause.addAndCondition("CDREQUISICAOSERV = ", requisicaoServImagem.cdRequisicaoServ);
		sqlWhereClause.addAndCondition("NMIMAGEM = ", requisicaoServImagem.nmImagem);
		//--
		sql.append(sqlWhereClause.getSql());
    }

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RequisicaoServImagem();
	}
    
}