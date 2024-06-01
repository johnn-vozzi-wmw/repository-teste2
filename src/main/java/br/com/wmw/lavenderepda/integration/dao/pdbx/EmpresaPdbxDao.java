package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import totalcross.sql.ResultSet;

public class EmpresaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Empresa();
	}

    private static EmpresaPdbxDao instance;

    public EmpresaPdbxDao() {
        super(Empresa.TABLE_NAME);
    }

    public static EmpresaPdbxDao getInstance() {
        if (instance == null) {
            instance = new EmpresaPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowKey,");
        sql.append(" CDEMPRESA,");
        sql.append(" NMEMPRESA,");
        sql.append(" NMEMPRESACURTO,");
        sql.append(" NUCNPJ,");
        sql.append(" NUINSCRICAOESTADUAL,");
        sql.append(" CDUSUARIO,");
        sql.append(" DSLOGRADOURO,");
        sql.append(" DSBAIRRO,");
        sql.append(" DSCEP,");
        sql.append(" DSCIDADE,");
        sql.append(" DSESTADO,");
        sql.append(" NUFONE,");
        sql.append(" VLPCTPIS,");
        sql.append(" VLPCTCOFINS,");
        sql.append(" VLPCTIRPJ,");
        if (LavenderePdaConfig.usaControlePesoPedidoPorEmpresa) {
        	sql.append(" VLMAXPESO,");
        	sql.append(" VLMINPESO,");
        }
        sql.append(" tb.DSURLCONSULTA,");
        sql.append(" tb.NUCSC,");
        sql.append(" tb.VLPCTCUSTOVARIAVEL,");
        sql.append(" tb.VLPCTCUSTOFIXO,");
        sql.append(" tb.VLPCTCSLL,");
        sql.append(" tb.VLPCTCPP,");
        sql.append(" tb.NULOGRADOURO,");
        sql.append(" tb.CDLOCALESTOQUE,");
        sql.append(" tb.NUDIASINTDEVOLESTOQUE,");
        if (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 8) {
        	sql.append(" tb.VLPCTCOMISSAO,");
        }
        sql.append(" tb.NUCIP,");
        sql.append(" tb.VLFATORFACEAMENTO,");
        sql.append(" tb.CDLOCAL,");
        sql.append(" tb.CDESTADO");
        
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Empresa empresa = new Empresa();
        empresa.rowKey = rs.getString("rowkey");
        empresa.cdEmpresa = rs.getString("cdEmpresa");
        empresa.nmEmpresa = rs.getString("nmEmpresa");
        empresa.nmEmpresaCurto = rs.getString("nmEmpresaCurto");
        empresa.nuCnpj = rs.getString("nuCnpj");
        empresa.nuInscricaoEstadual = rs.getString("nuInscricaoEstadual");
        empresa.cdUsuario = rs.getString("cdUsuario");
        empresa.dsLogradouro = rs.getString("dsLogradouro");
        empresa.dsBairro = rs.getString("dsBairro");
        empresa.dsCep = rs.getString("dsCep");
        empresa.dsCidade = rs.getString("dsCidade");
        empresa.dsEstado = rs.getString("dsEstado");
        if (LavenderePdaConfig.usaControlePesoPedidoPorEmpresa) {
        	empresa.vlMaxPeso = rs.getDouble("vlMaxPeso");
        	empresa.vlMinPeso = rs.getDouble("vlMinPeso");
        }
        empresa.nuFone = rs.getString("nuFone");
        empresa.nuCsc = rs.getString("nuCsc");
        empresa.vlPctPis = rs.getDouble("vlPctPis");
        empresa.vlPctCofins = rs.getDouble("vlPctCofins");
        empresa.vlPctIrpj = rs.getDouble("vlPctIrpj");
        empresa.vlPctCustoVariavel = rs.getDouble("vlPctCustoVariavel");
        empresa.vlPctCustoFixo = rs.getDouble("vlPctCustoFixo");
        empresa.vlPctCsll = rs.getDouble("vlPctCsll");
        empresa.vlPctCpp = rs.getDouble("vlPctCpp");	
        empresa.nuLogradouro = rs.getString("nuLogradouro");
        empresa.nuCip = rs.getString("nuCip");
        empresa.cdLocalEstoque = rs.getString("cdLocalEstoque");
        empresa.nuDiasIntDevolEstoque = rs.getInt("nuDiasIntDevolEstoque");
        if (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 8) {
        	empresa.vlPctComissao = rs.getDouble("vlPctComissao");
        }
        empresa.cdLocal = rs.getString("cdLocal");
        empresa.vlFatorFaceamento = rs.getDouble("vlFatorFaceamento");
        empresa.dsUrlConsulta = rs.getString("dsUrlConsulta");
        empresa.cdEstado = rs.getString("cdEstado");
        return empresa;
    }

	public double findVlPctFreteByCdEmpresa(String cdEmpresa) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("select vlPctFrete from ").append(tableName);
		sql.append(" where cdEmpresa = ").append(Sql.getValue(cdEmpresa));
		return getDouble(sql.toString());
	}

}