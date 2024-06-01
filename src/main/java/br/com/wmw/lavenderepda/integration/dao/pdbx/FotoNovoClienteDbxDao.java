package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.FotoNovoCliente;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class FotoNovoClienteDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FotoNovoCliente();
	}

    private static FotoNovoClienteDbxDao instance;
	

    public FotoNovoClienteDbxDao() {
        super(FotoNovoCliente.TABLE_NAME);
    }
    
    public static FotoNovoClienteDbxDao getInstance() {
        if (instance == null) {
            instance = new FotoNovoClienteDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        FotoNovoCliente fotoNovoCliente = new FotoNovoCliente();
        fotoNovoCliente.rowKey = rs.getString("rowkey");
        fotoNovoCliente.cdEmpresa = rs.getString("cdEmpresa");
        fotoNovoCliente.cdRepresentante = rs.getString("cdRepresentante");
        fotoNovoCliente.cdCliente = rs.getString("cdCliente");
        fotoNovoCliente.nmFoto = rs.getString("nmFoto");
        fotoNovoCliente.nuTamanho = rs.getInt("nuTamanho");
        fotoNovoCliente.dtModificacao = rs.getDate("dtModificacao");
        fotoNovoCliente.flTipoAlteracao = rs.getString("flTipoAlteracao");
        fotoNovoCliente.nuCarimbo = rs.getInt("nuCarimbo");
        fotoNovoCliente.cdUsuario = rs.getString("cdUsuario");
        return fotoNovoCliente;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDCLIENTE,");
        sql.append(" tb.NMFOTO,");
        sql.append(" tb.NUTAMANHO,");
        sql.append(" tb.DTMODIFICACAO,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.NUCARIMBO,");
        sql.append(" tb.CDUSUARIO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" NMFOTO,");
        sql.append(" NUTAMANHO,");
        sql.append(" DTMODIFICACAO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        FotoNovoCliente fotoNovoCliente = (FotoNovoCliente) domain;
        sql.append(Sql.getValue(fotoNovoCliente.cdEmpresa)).append(",");
        sql.append(Sql.getValue(fotoNovoCliente.cdRepresentante)).append(",");
        sql.append(Sql.getValue(fotoNovoCliente.cdCliente)).append(",");
        sql.append(Sql.getValue(fotoNovoCliente.nmFoto)).append(",");
        sql.append(Sql.getValue(fotoNovoCliente.nuTamanho)).append(",");
        sql.append(Sql.getValue(fotoNovoCliente.dtModificacao)).append(",");
        sql.append(Sql.getValue(fotoNovoCliente.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(fotoNovoCliente.nuCarimbo)).append(",");
        sql.append(Sql.getValue(fotoNovoCliente.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        FotoNovoCliente fotoNovoCliente = (FotoNovoCliente) domain;
        sql.append(" NUTAMANHO = ").append(Sql.getValue(fotoNovoCliente.nuTamanho)).append(",");
        sql.append(" DTMODIFICACAO = ").append(Sql.getValue(fotoNovoCliente.dtModificacao)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(fotoNovoCliente.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(fotoNovoCliente.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(fotoNovoCliente.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        FotoNovoCliente fotoNovoCliente = (FotoNovoCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", fotoNovoCliente.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", fotoNovoCliente.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", fotoNovoCliente.cdCliente);
		sqlWhereClause.addAndCondition("NMFOTO = ", fotoNovoCliente.nmFoto);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    public Vector findAllAlterados() throws SQLException {
    	if (LavenderePdaConfig.usaValidacaoCPFCNPJBaseWeb) {
    		StringBuffer sql = getSqlBuffer();
    		sql.append(" select ");
    		addSelectColumns(null, sql);
    		sql.append(" from ")
    		.append(tableName)
    		.append(" tb ")
    		.append("JOIN TBLVPNOVOCLIENTE cli ON ")
    		.append(" tb.CDEMPRESA = cli.CDEMPRESA AND ")
    		.append("tb.CDREPRESENTANTE = cli.CDREPRESENTANTE AND ")
    		.append("tb.CDCLIENTE = cli.CDNOVOCLIENTE AND ")
    		.append("cli.FLSTATUSCADASTRO <> 'P'")
    		.append(" where tb.fltipoalteracao != ")
        	.append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ORIGINAL));
        	return findAll(null, sql.toString());
    	} else {
    		return super.findAllAlterados();
    	}
    }
    
    public static String getSqlDadosEnvioServidor() {
    	StringBuffer sb = new StringBuffer();
    	sb.append("SELECT tb.* FROM TBLVPFOTONOVOCLIENTE tb ")
    	.append("JOIN TBLVPNOVOCLIENTE cli ON ")
    	.append("tb.CDEMPRESA = cli.CDEMPRESA AND ")
    	.append("tb.CDREPRESENTANTE = cli.CDREPRESENTANTE AND ")
    	.append("tb.CDCLIENTE = cli.CDNOVOCLIENTE AND ")
    	.append("cli.FLTIPOALTERACAO = ''")
    	.append(" WHERE tb.").append(FotoNovoCliente.NMCAMPOTIPOALTERACAO)
    	.append(" <> ").append(Sql.getValue(FotoNovoCliente.FLTIPOALTERACAO_ORIGINAL));
    	return sb.toString(); 
    }
    
}