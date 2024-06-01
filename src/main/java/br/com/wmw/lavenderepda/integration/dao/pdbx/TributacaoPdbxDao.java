package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Tributacao;
import totalcross.sql.ResultSet;

public class TributacaoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Tributacao();
	}

    private static TributacaoPdbxDao instance;

    public TributacaoPdbxDao() {
        super(Tributacao.TABLE_NAME);
    }

    public static TributacaoPdbxDao getInstance() {
        if (instance == null) {
            instance = new TributacaoPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
	
    //@Override
  	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
  		return null;
  	}

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Tributacao tributacao = new Tributacao();
        tributacao.rowKey = rs.getString("rowkey");
        tributacao.cdEmpresa = rs.getString("cdEmpresa");
        tributacao.cdRepresentante = rs.getString("cdRepresentante");
        tributacao.cdTributacaoCliente = rs.getString("cdTributacaoCliente");
        tributacao.cdTributacaoProduto = rs.getString("cdTributacaoProduto");
        tributacao.cdTipoPedido = rs.getString("cdTipoPedido");
        tributacao.cdUf = rs.getString("cdUf");
        tributacao.vlPctRepasseIcms = ValueUtil.round(rs.getDouble("vlPctRepasseIcms"));
        tributacao.vlPctReducaoBaseIcms = ValueUtil.round(rs.getDouble("vlPctReducaoBaseIcms"));
        tributacao.vlPctIcms = ValueUtil.round(rs.getDouble("vlPctIcms"));
        tributacao.cdTipoRecolhimento = rs.getInt("cdTipoRecolhimento");
        tributacao.flBaseIpiComDesconto = rs.getString("flBaseIpiComDesconto");
        tributacao.flBaseIcmsRetidoComDesconto = rs.getString("flBaseIcmsRetidoComDesconto");
        tributacao.flBaseIcmsRetidoComRepasse = rs.getString("flBaseIcmsRetidoComRepasse");
        tributacao.vlPctMargemAgregada = ValueUtil.round(rs.getDouble("vlPctMargemAgregada"));
        tributacao.vlPctIcmsRetido = ValueUtil.round(rs.getDouble("vlPctIcmsRetido"));
        tributacao.vlPctReducaoBaseIcmsRetido = ValueUtil.round(rs.getDouble("vlPctReducaoBaseIcmsRetido"));
        tributacao.vlPctReducaoIcms = ValueUtil.round(rs.getDouble("vlPctReducaoIcms"));
        tributacao.vlPctMinSt = ValueUtil.round(rs.getDouble("vlPctMinSt"));
        tributacao.flAplicaIpiIcms = rs.getString("flAplicaIpiIcms");
        tributacao.flAplicaIpiRetido = rs.getString("flAplicaIpiRetido");
        tributacao.vlPctFecop = rs.getDouble("vlPctFecop");
        tributacao.vlPctFecopRecolher = rs.getDouble("vlPctFecopRecolher");
        tributacao.vlDespesaAcessoria = rs.getDouble("vlDespesaAcessoria");
        tributacao.flAplicaFrete = rs.getString("flAplicaFrete");
        tributacao.flAplicaFecopST = rs.getString("flAplicaFecopST");
        tributacao.cdTipoFecop = rs.getInt("cdTipoFecop");
        tributacao.vlPctPis = rs.getDouble("vlPctPis");
        tributacao.vlPctCofins = rs.getDouble("vlPctCofins");
        tributacao.flUsaIpiCalculado = rs.getString("flUsaIpiCalculado");
        tributacao.vlMinPis = rs.getDouble("vlMinPis");
        tributacao.vlMinCofins = rs.getDouble("vlMinCofins");
        tributacao.vlPctIpi = rs.getDouble("vlPctIpi");
        tributacao.vlMinIpi = rs.getDouble("vlMinIpi");
        tributacao.vlPctOutorga = rs.getDouble("vlPctOutorga");
       	tributacao.vlIcms = rs.getDouble("vlIcms");
       	tributacao.vlIcmsRetido = rs.getDouble("vlIcmsRetido");
        tributacao.flMedicamento = rs.getString("flMedicamento");
        tributacao.cdBeneficioFiscal = rs.getString("cdBeneficioFiscal");
        tributacao.vlPctDiferenca = rs.getDouble("vlPctDiferenca");
        return tributacao;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" cdTributacaoCliente,");
        sql.append(" cdTributacaoProduto,");
        sql.append(" cdUf,");
        sql.append(" cdTipoPedido,");
        sql.append(" vlPctRepasseIcms,");
        sql.append(" vlPctReducaoBaseIcms,");
        sql.append(" vlPctIcms,");
        sql.append(" cdTipoRecolhimento,");
        sql.append(" flBaseIpiComDesconto,");
        sql.append(" flBaseIcmsRetidoComDesconto,");
        sql.append(" flBaseIcmsRetidoComRepasse,");
        sql.append(" vlPctMargemAgregada,");
        sql.append(" vlPctIcmsRetido,");
        sql.append(" vlPctReducaoBaseIcmsRetido,");
        sql.append(" vlPctReducaoIcms,");
        sql.append(" vlPctMinSt,");
        sql.append(" flAplicaIpiIcms,");
        sql.append(" flAplicaIpiRetido,");
        sql.append(" vlPctFecop,");
        sql.append(" vlPctFecopRecolher,");
        sql.append(" vlDespesaAcessoria,");
        sql.append(" flAplicaFrete,");
        sql.append(" flAplicaFecopST,");
        sql.append(" cdTipoFecop,");
        sql.append(" vlPctPis,");
        sql.append(" vlPctCofins,");
        sql.append(" flUsaIpiCalculado,");
        sql.append(" vlMinPis,");
        sql.append(" vlPctIpi,");
        sql.append(" vlMinIpi,");
        sql.append(" vlPctOutorga,");
       	sql.append(" vlIcms,");
       	sql.append(" vlIcmsRetido,");
        sql.append(" vlMinCofins,");
        sql.append(" flMedicamento,");
        sql.append(" cdBeneficioFiscal,");
        sql.append(" vlPctDiferenca");
    }
    
    @Override
    protected void addCacheColumns(StringBuffer sql) throws SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" cdTributacaoCliente,");
        sql.append(" cdTributacaoProduto,");
        sql.append(" cdUf,");
        sql.append(" cdTipoPedido,");
        sql.append(" vlPctRepasseIcms,");
        sql.append(" vlPctReducaoBaseIcms,");
        sql.append(" vlPctIcms,");
        sql.append(" cdTipoRecolhimento,");
        sql.append(" flBaseIcmsRetidoComDesconto,");
        sql.append(" flBaseIcmsRetidoComRepasse,");
        sql.append(" vlPctMargemAgregada,");
        sql.append(" vlPctIcmsRetido,");
        sql.append(" vlPctReducaoBaseIcmsRetido,");
        sql.append(" vlPctReducaoIcms,");
        sql.append(" vlPctMinSt,");
        sql.append(" flAplicaIpiIcms,");
        sql.append(" flAplicaIpiRetido,");
        sql.append(" vlPctFecop,");
        sql.append(" vlPctFecopRecolher,");
        sql.append(" vlDespesaAcessoria,");
        sql.append(" flAplicaFrete,");
        sql.append(" flAplicaFecopST,");
        sql.append(" cdTipoFecop,");
        sql.append(" vlPctPis,");
        sql.append(" vlPctCofins,");
        sql.append(" flUsaIpiCalculado,");
        sql.append(" vlMinPis,");
        sql.append(" vlPctIpi,");
        sql.append(" vlMinIpi,");
        sql.append(" vlPctOutorga,");
       	sql.append(" vlIcms,");
       	sql.append(" vlIcmsRetido,");
        sql.append(" vlMinCofins,");
        sql.append(" flMedicamento,");
        sql.append(" cdBeneficioFiscal,");
        sql.append(" vlPctDiferenca,");
        sql.append(" flBaseIpiComDesconto");
    }
    
    @Override
    protected BaseDomain populateCache(ResultSet rs) throws SQLException {
    	 Tributacao tributacao = new Tributacao();
         tributacao.rowKey = rs.getString(1);        
         tributacao.cdEmpresa = rs.getString(2);
         tributacao.cdRepresentante = rs.getString(3);
         tributacao.cdTributacaoCliente = rs.getString(4);
         tributacao.cdTributacaoProduto = rs.getString(5);
         tributacao.cdUf = rs.getString(6);
         tributacao.cdTipoPedido = rs.getString(7);
         tributacao.vlPctRepasseIcms = rs.getDouble(8);
         tributacao.vlPctReducaoBaseIcms = rs.getDouble(9);
         tributacao.vlPctIcms = rs.getDouble(10);
         tributacao.cdTipoRecolhimento = rs.getInt(11);
         tributacao.flBaseIcmsRetidoComDesconto = rs.getString(12);
         tributacao.flBaseIcmsRetidoComRepasse = rs.getString(13);
         tributacao.vlPctMargemAgregada = rs.getDouble(14);
         tributacao.vlPctIcmsRetido = rs.getDouble(15);
         tributacao.vlPctReducaoBaseIcmsRetido = rs.getDouble(16);
         tributacao.vlPctReducaoIcms = rs.getDouble(17);
         tributacao.vlPctMinSt = rs.getDouble(18);
         tributacao.flAplicaIpiIcms = rs.getString(19);
         tributacao.flAplicaIpiRetido = rs.getString(20);
         tributacao.vlPctFecop = rs.getDouble(21);
         tributacao.vlPctFecopRecolher = rs.getDouble(22);
         tributacao.vlDespesaAcessoria = rs.getDouble(23);
         tributacao.flAplicaFrete = rs.getString(24);
         tributacao.flAplicaFecopST = rs.getString(25);
         tributacao.cdTipoFecop = rs.getInt(26);
         tributacao.vlPctPis = rs.getDouble(27);
         tributacao.vlPctCofins = rs.getDouble(28);
         tributacao.flUsaIpiCalculado = rs.getString(29);
         tributacao.vlMinPis = rs.getDouble(30);
         tributacao.vlPctIpi = rs.getDouble(31);
         tributacao.vlMinIpi = rs.getDouble(32);
         tributacao.vlPctOutorga = rs.getDouble(33);
         tributacao.vlIcms = rs.getDouble(34);
         tributacao.vlIcmsRetido = rs.getDouble(35);
         tributacao.vlMinCofins = rs.getDouble(36);
         tributacao.flMedicamento = rs.getString(37);
         tributacao.cdBeneficioFiscal = rs.getString(38);
         tributacao.vlPctDiferenca = rs.getDouble(39);
         tributacao.flBaseIpiComDesconto = rs.getString(40);
         return tributacao;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Tributacao tributacao = (Tributacao) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", tributacao.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", tributacao.cdRepresentante);
		if (ValueUtil.isNotEmpty(tributacao.cdTributacaoClienteFilter)) {
			sqlWhereClause.addAndConditionIn("cdTributacaoCliente", tributacao.cdTributacaoClienteFilter);
		} else {
			sqlWhereClause.addAndCondition("cdTributacaoCliente = ", tributacao.cdTributacaoCliente);
		}
		sqlWhereClause.addAndCondition("cdTributacaoProduto = ", tributacao.cdTributacaoProduto);
		sqlWhereClause.addAndCondition("cdTipoPedido = ", tributacao.cdTipoPedido);
		sqlWhereClause.addAndCondition("cdUf = ", tributacao.cdUf);
		sql.append(sqlWhereClause.getSql());
    }
	
}