package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Representante;
import totalcross.sql.ResultSet;

public class RepresentantePdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Representante();
	}

    private static RepresentantePdbxDao instance;

    public RepresentantePdbxDao() {
        super(Representante.TABLE_NAME);
    }

    public static RepresentantePdbxDao getInstance() {
        if (instance == null) {
            instance = new RepresentantePdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
	    Representante representante = new Representante();
	    representante.rowKey = rs.getString("rowkey");
	    representante.cdRepresentante = rs.getString("cdRepresentante");
	    representante.nmRepresentante = rs.getString("nmRepresentante");
	    representante.vlToleranciaVerba = rs.getDouble("vlToleranciaVerba");
	    representante.flEspecial = rs.getString("flEspecial");
	    representante.vlPctDescEspecial = rs.getDouble("vlPctDescEspecial");
	    representante.vlPctMaxDesconto = rs.getDouble("vlPctMaxDesconto");
	    representante.flTipoCadastro = rs.getString("flTipoCadastro");
	    representante.nuNotaGeral = rs.getString("nuNotaGeral");
	    if (LavenderePdaConfig.usaCodigoRepresentanteErp) {
	    	representante.cdRepresentanteErp = rs.getString("cdRepresentanteErp");
	    }
        representante.flRepresentanteInterno = rs.getString("flRepresentanteInterno");
	    return representante;
    }

    //@Override
    protected BaseDomain populateCache(ResultSet rs) throws java.sql.SQLException {
        Representante representante = new Representante();
        representante.rowKey = rs.getString("rowKey");
        representante.cdRepresentante = rs.getString("cdRepresentante");
        representante.nmRepresentante = rs.getString("nmRepresentante");
        representante.vlToleranciaVerba = rs.getDouble("vlToleranciaVerba");
	    representante.flEspecial = rs.getString("flEspecial");
	    representante.vlPctDescEspecial = rs.getDouble("vlPctDescEspecial");
	    representante.vlPctMaxDesconto = rs.getDouble("vlPctMaxDesconto");
	    representante.nuNotaGeral = rs.getString("nuNotaGeral");
	    if (LavenderePdaConfig.usaCodigoRepresentanteErp) {
	    	representante.cdRepresentanteErp = rs.getString("cdRepresentanteErp");
	    }
        representante.flRepresentanteInterno = rs.getString("flRepresentanteInterno");
        return representante;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowKey,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" NMREPRESENTANTE,");
        sql.append(" VLTOLERANCIAVERBA,");
		sql.append(" FLESPECIAL,");
		sql.append(" VLPCTDESCESPECIAL,");
		sql.append(" VLPCTMAXDESCONTO,");
        sql.append(" FLTIPOCADASTRO,");
        sql.append(" FLREPRESENTANTEINTERNO,");
        sql.append(" NUNOTAGERAL");
        if (LavenderePdaConfig.usaCodigoRepresentanteErp) {
        	sql.append(" ,CDREPRESENTANTEERP");
        }
    }

    //@Override
    protected void addCacheColumns(StringBuffer sql) {
        sql.append(" CDREPRESENTANTE,");
        sql.append(" NMREPRESENTANTE,");
		sql.append(" FLESPECIAL,");
		sql.append(" VLPCTDESCESPECIAL,");
		sql.append(" VLPCTMAXDESCONTO,");
        sql.append(" VLTOLERANCIAVERBA,");
        sql.append(" FLREPRESENTANTEINTERNO,");
        sql.append(" NUNOTAGERAL");
        if (LavenderePdaConfig.usaCodigoRepresentanteErp) {
        	sql.append(" ,CDREPRESENTANTEERP");
        }
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Representante representante = (Representante) domain;
        sql.append(" where CDREPRESENTANTE = ").append(Sql.getValue(representante.cdRepresentante));
    }
}