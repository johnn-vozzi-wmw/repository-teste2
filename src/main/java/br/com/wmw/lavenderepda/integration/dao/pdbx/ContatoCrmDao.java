package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ContatoCrm;
import totalcross.sql.ResultSet;

public class ContatoCrmDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ContatoCrm();
	}

    private static ContatoCrmDao instance;
	

    public ContatoCrmDao() {
        super(ContatoCrm.TABLE_NAME);
    }
    
    public static ContatoCrmDao getInstance() {
        if (instance == null) {
            instance = new ContatoCrmDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ContatoCrm contatocrm = new ContatoCrm();
        contatocrm.rowKey = rs.getString("rowkey");
        contatocrm.cdEmpresa = rs.getString("cdEmpresa");
        contatocrm.cdRepresentante = rs.getString("cdRepresentante");
        contatocrm.cdCliente = rs.getString("cdCliente");
        contatocrm.cdContato = rs.getString("cdContato");
        contatocrm.nmContato = rs.getString("nmContato");
        contatocrm.nuFone = rs.getString("nuFone");
        contatocrm.dsEmail = rs.getString("dsEmail");
        contatocrm.nuCarimbo = rs.getInt("nuCarimbo");
        contatocrm.flTipoAlteracao = rs.getString("flTipoAlteracao");
        contatocrm.cdUsuario = rs.getString("cdUsuario");
        return contatocrm;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDCONTATO,");
        sql.append(" NMCONTATO,");
        sql.append(" NUFONE,");
        sql.append(" DSEMAIL,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDCONTATO,");
        sql.append(" NMCONTATO,");
        sql.append(" NUFONE,");
        sql.append(" DSEMAIL,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ContatoCrm contatocrm = (ContatoCrm) domain;
        sql.append(Sql.getValue(contatocrm.cdEmpresa)).append(",");
        sql.append(Sql.getValue(contatocrm.cdRepresentante)).append(",");
        sql.append(Sql.getValue(contatocrm.cdCliente)).append(",");
        sql.append(Sql.getValue(contatocrm.cdContato)).append(",");
        sql.append(Sql.getValue(contatocrm.nmContato)).append(",");
        sql.append(Sql.getValue(contatocrm.nuFone)).append(",");
        sql.append(Sql.getValue(contatocrm.dsEmail)).append(",");
        sql.append(Sql.getValue(contatocrm.nuCarimbo)).append(",");
        sql.append(Sql.getValue(contatocrm.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(contatocrm.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ContatoCrm contatocrm = (ContatoCrm) domain;
        sql.append(" NMCONTATO = ").append(Sql.getValue(contatocrm.nmContato)).append(",");
        sql.append(" NUFONE = ").append(Sql.getValue(contatocrm.nuFone)).append(",");
        sql.append(" DSEMAIL = ").append(Sql.getValue(contatocrm.dsEmail)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(contatocrm.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(contatocrm.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(contatocrm.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ContatoCrm contatocrm = (ContatoCrm) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", contatocrm.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", contatocrm.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", contatocrm.cdCliente);
		sqlWhereClause.addAndCondition("CDCONTATO = ", contatocrm.cdContato);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}