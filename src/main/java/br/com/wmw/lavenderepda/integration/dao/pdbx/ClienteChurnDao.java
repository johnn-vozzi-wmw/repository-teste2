package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ClienteChurn;
import br.com.wmw.lavenderepda.business.domain.MotivoChurn;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;

public class ClienteChurnDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ClienteChurn();
	}

    private static ClienteChurnDao instance = null;

    public ClienteChurnDao() {
        super(ClienteChurn.TABLE_NAME);
    }
    
    public static ClienteChurnDao getInstance() {
        if (instance == null) {
            instance = new ClienteChurnDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        ClienteChurn clienteChurn = new ClienteChurn();
        clienteChurn.rowKey = rs.getString("rowkey");
        clienteChurn.cdEmpresa = rs.getString("cdEmpresa");
        clienteChurn.cdRepresentante = rs.getString("cdRepresentante");
        clienteChurn.cdChurn = rs.getString("cdChurn");
        clienteChurn.cdCliente = rs.getString("cdCliente");
        clienteChurn.dtEntradaChurn = rs.getDate("dtEntradaChurn");
        clienteChurn.dsObsChurn = rs.getString("dsObsChurn");
        clienteChurn.cdUsuario = rs.getString("cdUsuario");
        clienteChurn.nuCarimbo = rs.getInt("nuCarimbo");
        clienteChurn.flTipoAlteracao = rs.getString("flTipoAlteracao");
        clienteChurn.dtAlteracao = rs.getDate("dtAlteracao");
        clienteChurn.hrAlteracao = rs.getString("hrAlteracao");
        clienteChurn.getCliente().cdEmpresa = rs.getString("cdEmpresa");
        clienteChurn.getCliente().cdRepresentante = rs.getString("cdRepresentante");
        clienteChurn.getCliente().cdCliente = rs.getString("cdCliente");
        clienteChurn.getCliente().nmRazaoSocial = rs.getString("nmRazaoSocial");
        setCdMotivoChurn(clienteChurn, rs);
        return clienteChurn;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDCHURN,");
        sql.append(" tb.CDCLIENTE,");
        sql.append(" tb.DTENTRADACHURN,");
        sql.append(" tb.CDMOTIVOCHURN,");
        sql.append(" tb.DSOBSCHURN,");
        sql.append(" tb.CDUSUARIO,");
        sql.append(" tb.NUCARIMBO,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.DTALTERACAO,");
        sql.append(" tb.HRALTERACAO," );
        sql.append(DaoUtil.ALIAS_CLIENTE + ".NMRAZAOSOCIAL," );
        sql.append("M.DSMOTIVOCHURN" );
    }
    
    @Override
    protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
    	DaoUtil.addJoinCliente(sql);
    	sql.append(" LEFT JOIN TBLVPMOTIVOCHURN M ON ");
		sql.append("M.CDMOTIVOCHURN = tb.CDMOTIVOCHURN");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) { }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) { }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) { 
    	ClienteChurn clienteChurn = (ClienteChurn) domain;
    	sql.append(" CDMOTIVOCHURN = ").append(Sql.getValue(clienteChurn.cdMotivoChurn)).append(",");
        sql.append(" DSOBSCHURN = ").append(Sql.getValue(clienteChurn.dsObsChurn)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(clienteChurn.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(clienteChurn.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(clienteChurn.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(clienteChurn.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(clienteChurn.hrAlteracao));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        ClienteChurn clienteChurn = (ClienteChurn) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", clienteChurn.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", clienteChurn.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDCHURN = ", clienteChurn.cdChurn);
		sqlWhereClause.addAndCondition("tb.CDCLIENTE = ", clienteChurn.cdCliente);
		aplicaFiltroMotivoChurn(clienteChurn, sqlWhereClause);
		aplicaFiltroCliente(clienteChurn.dsFiltroCliente, sqlWhereClause);
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	if (ValueUtil.isEmpty(domain.sortAtributte)) return;
    	
    	sql.append("order by " + domain.sortAtributte);
    	sql.append(ValueUtil.VALOR_SIM.equals(domain.sortAsc) ? " ASC" : " DESC");
    }

	private void aplicaFiltroMotivoChurn(ClienteChurn clienteChurn, SqlWhereClause sqlWhereClause) {
		if (clienteChurn.getMotivoChurn().isSemMotivoInformado()) {
			sqlWhereClause.addAndCondition("(TB.CDMOTIVOCHURN is null or TB.CDMOTIVOCHURN = '')");
			return;
		}
		sqlWhereClause.addAndCondition("TB.CDMOTIVOCHURN = ", clienteChurn.cdMotivoChurn);
	}
	
	private void aplicaFiltroCliente(String dsFiltroCliente, SqlWhereClause sqlWhereClause) {
		if (ValueUtil.isEmpty(dsFiltroCliente)) return;
		
		dsFiltroCliente = "%" + dsFiltroCliente + "%";
		
		sqlWhereClause.addStartAndMultipleCondition();
       	
		sqlWhereClause.addAndLikeCondition(DaoUtil.ALIAS_CLIENTE + ".NMRAZAOSOCIAL", dsFiltroCliente, false);
       	sqlWhereClause.addOrLikeCondition(DaoUtil.ALIAS_CLIENTE + ".CDCLIENTE", dsFiltroCliente, false);
       	sqlWhereClause.addOrLikeCondition(DaoUtil.ALIAS_CLIENTE + ".NMFANTASIA", dsFiltroCliente, false);
   		
       	sqlWhereClause.addEndMultipleCondition();
	}
	
	private void setCdMotivoChurn(ClienteChurn clienteChurn, ResultSet rs) throws SQLException {
		String cdMotivoChurn = rs.getString("cdMotivoChurn");
		if (ValueUtil.isNotEmpty(cdMotivoChurn)) {
			clienteChurn.setMotivoChurn(cdMotivoChurn);
	        clienteChurn.getMotivoChurn().dsMotivoChurn = rs.getString("dsMotivoChurn");
	        return;
		}
		clienteChurn.setMotivoChurn(MotivoChurn.CD_SEM_MOTIVO_INFORMADO);
		clienteChurn.getMotivoChurn().dsMotivoChurn = MotivoChurn.DS_SEM_MOTIVO_INFORMADO;
	}

	public int getTotalGeralClienteChurn(ClienteChurn clienteChurn) throws SQLException {
		StringBuffer sql = new StringBuffer();
    	
		sql.append("SELECT count(distinct cdCliente) as qtde FROM ");
    	sql.append(tableName);
        sql.append(" tb ");
        
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", clienteChurn.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", clienteChurn.cdRepresentante);
		sql.append(sqlWhereClause.getSql());
		return getInt(sql.toString());
	}
	
	public int getTotalParcialClienteChurn(ClienteChurn clienteChurn) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(distinct tb.cdCliente) as qtde FROM ");
    	sql.append(tableName);
        sql.append(" tb ");
        addJoin(clienteChurn, sql);
        addWhereByExample(clienteChurn, sql);
    	return getInt(sql.toString());
	}
	
	public int getTotalClienteChurnComMotivoInformado(ClienteChurn clienteChurn) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(distinct tb.cdCliente) as qtde FROM ");
    	sql.append(tableName);
        sql.append(" tb ");
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", clienteChurn.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", clienteChurn.cdRepresentante);
		sqlWhereClause.addAndCondition("(tb.CDMOTIVOCHURN != NULL OR tb.CDMOTIVOCHURN != '')");
		return getInt(sql.toString());
	}
	
    
}