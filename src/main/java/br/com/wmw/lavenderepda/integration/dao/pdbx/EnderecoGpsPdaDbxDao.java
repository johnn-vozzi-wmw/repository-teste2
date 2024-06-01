package br.com.wmw.lavenderepda.integration.dao.pdbx;


import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.EnderecoGpsPda;
import totalcross.sql.ResultSet;

public class EnderecoGpsPdaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new EnderecoGpsPda();
	}

    private static EnderecoGpsPdaDbxDao instance;
	

    public EnderecoGpsPdaDbxDao() {
        super(EnderecoGpsPda.TABLE_NAME);
    }
    
    public static EnderecoGpsPdaDbxDao getInstance() {
        if (instance == null) {
            instance = new EnderecoGpsPdaDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        EnderecoGpsPda enderecoGpsPda = new EnderecoGpsPda();
        enderecoGpsPda.rowKey = rs.getString("rowkey");
        enderecoGpsPda.dsBairro = rs.getString("dsBairro");
        enderecoGpsPda.dsCidade = rs.getString("dsCidade");
        enderecoGpsPda.dsEstado = rs.getString("dsEstado");
        enderecoGpsPda.dsLogradouro = rs.getString("dsLogradouro");
        enderecoGpsPda.dsCep = rs.getString("dsCep");
        enderecoGpsPda.nuLogradouro = rs.getString("nuLogradouro");
        enderecoGpsPda.cdLatitude = ValueUtil.round(rs.getDouble("cdLatitude"));
        enderecoGpsPda.cdLongitude = ValueUtil.round(rs.getDouble("cdLongitude"));
        enderecoGpsPda.dtColeta = rs.getDate("dtColeta");
        enderecoGpsPda.hrColeta = rs.getString("hrColeta");
        enderecoGpsPda.cdCliente = rs.getString("cdCliente");
        enderecoGpsPda.nuCarimbo = rs.getInt("nuCarimbo");
        enderecoGpsPda.flTipoAlteracao = rs.getString("flTipoAlteracao");
        enderecoGpsPda.cdUsuario = rs.getString("cdUsuario");
        return enderecoGpsPda;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" DSBAIRRO,");
        sql.append(" DSCIDADE,");
        sql.append(" DSESTADO,");
        sql.append(" DSLOGRADOURO,");
        sql.append(" DSCEP,");
        sql.append(" NULOGRADOURO,");
        sql.append(" CDLATITUDE,");
        sql.append(" CDLONGITUDE,");
        sql.append(" DTCOLETA,");
        sql.append(" HRCOLETA,");
        sql.append(" CDCLIENTE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" DSBAIRRO,");
        sql.append(" DSCIDADE,");
        sql.append(" DSESTADO,");
        sql.append(" DSLOGRADOURO,");
        sql.append(" DSCEP,");
        sql.append(" NULOGRADOURO,");
        sql.append(" CDLATITUDE,");
        sql.append(" CDLONGITUDE,");
        sql.append(" DTCOLETA,");
        sql.append(" HRCOLETA,");
        sql.append(" CDCLIENTE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        EnderecoGpsPda enderecoGpsPda = (EnderecoGpsPda) domain;
        sql.append(Sql.getValue(enderecoGpsPda.dsBairro)).append(",");
        sql.append(Sql.getValue(enderecoGpsPda.dsCidade)).append(",");
        sql.append(Sql.getValue(enderecoGpsPda.dsEstado)).append(",");
        sql.append(Sql.getValue(enderecoGpsPda.dsLogradouro)).append(",");
        sql.append(Sql.getValue(enderecoGpsPda.dsCep)).append(",");
        sql.append(Sql.getValue(enderecoGpsPda.nuLogradouro)).append(",");
        sql.append(Sql.getValue(enderecoGpsPda.cdLatitude)).append(",");
        sql.append(Sql.getValue(enderecoGpsPda.cdLongitude)).append(",");
        sql.append(Sql.getValue(enderecoGpsPda.dtColeta)).append(",");
        sql.append(Sql.getValue(enderecoGpsPda.hrColeta)).append(",");
        sql.append(Sql.getValue(enderecoGpsPda.cdCliente)).append(",");
        sql.append(Sql.getValue(enderecoGpsPda.nuCarimbo)).append(",");
        sql.append(Sql.getValue(enderecoGpsPda.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(enderecoGpsPda.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        EnderecoGpsPda enderecoGpsPda = (EnderecoGpsPda) domain;
        sql.append(" CDLATITUDE = ").append(Sql.getValue(enderecoGpsPda.cdLatitude)).append(",");
        sql.append(" CDLONGITUDE = ").append(Sql.getValue(enderecoGpsPda.cdLongitude)).append(",");
        sql.append(" DTCOLETA = ").append(Sql.getValue(enderecoGpsPda.dtColeta)).append(",");
        sql.append(" HRCOLETA = ").append(Sql.getValue(enderecoGpsPda.hrColeta)).append(",");
        sql.append(" CDCLIENTE = ").append(Sql.getValue(enderecoGpsPda.cdCliente)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(enderecoGpsPda.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(enderecoGpsPda.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(enderecoGpsPda.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        EnderecoGpsPda enderecoGpsPda = (EnderecoGpsPda) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("DSBAIRRO = ", enderecoGpsPda.dsBairro);
		sqlWhereClause.addAndCondition("DSCIDADE = ", enderecoGpsPda.dsCidade);
		sqlWhereClause.addAndCondition("DSESTADO = ", enderecoGpsPda.dsEstado);
		sqlWhereClause.addAndCondition("DSLOGRADOURO = ", enderecoGpsPda.dsLogradouro);
		sqlWhereClause.addAndCondition("DSCEP = ", enderecoGpsPda.dsCep);
		sqlWhereClause.addAndCondition("NULOGRADOURO = ", enderecoGpsPda.nuLogradouro);
		sqlWhereClause.addAndCondition("FLTIPOALTERACAO = ", enderecoGpsPda.flTipoAlteracao);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", enderecoGpsPda.cdCliente);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    //@Override
    protected void addWhereDeleteByExample(BaseDomain domain, StringBuffer sql) {
    	EnderecoGpsPda enderecoGpsPda = (EnderecoGpsPda) domain;
    	SqlWhereClause sqlWhereClause = new SqlWhereClause();
    	sqlWhereClause.addAndConditionForced("FLTIPOALTERACAO = ", enderecoGpsPda.flTipoAlteracao == null ? Sql.getValue(enderecoGpsPda.flTipoAlteracao) : enderecoGpsPda.flTipoAlteracao);
    	sql.append(sqlWhereClause.getSql());
    }
}