package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import totalcross.sql.ResultSet;
import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Nfce;

public class NfceDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Nfce();
	}

    private static NfceDbxDao instance = null;
	public static String TABLE_NAME = "TBLVPNFCE";

    public NfceDbxDao() {
        super(TABLE_NAME);
    }
    
    public static NfceDbxDao getInstance() {
        if (instance == null) {
            instance = new NfceDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        Nfce nfce = new Nfce();
        nfce.rowKey = rs.getString("rowkey");
        nfce.cdEmpresa = rs.getString("cdEmpresa");
        nfce.cdRepresentante = rs.getString("cdRepresentante");
        nfce.nuPedido = rs.getString("nuPedido");
        nfce.flOrigemPedido = rs.getString("flOrigemPedido");
        nfce.vlTotalNfce = ValueUtil.round(rs.getDouble("vlTotalNfce"));
        nfce.vlTotalDesconto = ValueUtil.round(rs.getDouble("vlTotalDesconto"));
        nfce.vlTotalLiquidoNfce = ValueUtil.round(rs.getDouble("vlTotalLiquidoNfce"));
        nfce.dsFormaPagamento = rs.getString("dsFormaPagamento");
        nfce.vlTotalPago = ValueUtil.round(rs.getDouble("vlTotalPago"));
        nfce.vlTroco = ValueUtil.round(rs.getDouble("vlTroco"));
        nfce.nuChaveAcesso = rs.getString("nuChaveAcesso");
        nfce.nuNfce = rs.getString("nuNfce");
        nfce.nuSerie = rs.getString("nuSerie");
        nfce.dtEmissao = rs.getDate("dtEmissao");
        nfce.hrEmissao = rs.getString("hrEmissao");
        nfce.nuProtocoloAutorizacao = rs.getString("nuProtocoloAutorizacao");
        nfce.dtAutorizacao = rs.getDate("dtAutorizacao");
        nfce.hrAutorizacao = rs.getString("hrAutorizacao");
        nfce.vlTotalTributos = ValueUtil.round(rs.getDouble("vlTotalTributos"));
        nfce.vlPctTributosFederais = ValueUtil.round(rs.getDouble("vlPctTributosFederais"));
        nfce.vlPctTributosEstaduais = ValueUtil.round(rs.getDouble("vlPctTributosEstaduais"));
        nfce.vlPctTributosMunicipais = ValueUtil.round(rs.getDouble("vlPctTributosMunicipais"));
        nfce.flTipoAlteracao = rs.getString("flTipoAlteracao");
        nfce.dtAlteracao = rs.getDate("dtAlteracao");
        nfce.hrAlteracao = rs.getString("hrAlteracao");
        nfce.cdTipoEmissao = rs.getString("cdTipoEmissao");
        nfce.nuVersaoQrCode = rs.getString("nuVersaoQrCode");
        nfce.nuAmbienteNfce = rs.getString("nuAmbienteNfce");
        nfce.dsDigestValueNfce = rs.getString("dsDigestValueNfce");
        nfce.cdIdentificacaoCsc = rs.getString("cdIdentificacaoCsc");
        nfce.dsUrlQrCode = rs.getString("dsUrlQrCode");
        nfce.nuCarimbo = rs.getInt("nuCarimbo");
        nfce.cdUsuario = rs.getString("cdUsuario");
        nfce.qtTotalItem = rs.getInt("qtTotalItem");
        nfce.cdMensagemRetorno = rs.getString("cdMensagemRetorno");
        nfce.dsMensagemRetorno = rs.getString("dsMensagemRetorno");
        return nfce;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" NUPEDIDO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" VLTOTALNFCE,");
        sql.append(" VLTOTALDESCONTO,");
        sql.append(" VLTOTALLIQUIDONFCE,");
        sql.append(" DSFORMAPAGAMENTO,");
        sql.append(" VLTOTALPAGO,");
        sql.append(" VLTROCO,");
        sql.append(" NUCHAVEACESSO,");
        sql.append(" NUNFCE,");
        sql.append(" NUSERIE,");
        sql.append(" DTEMISSAO,");
        sql.append(" HREMISSAO,");
        sql.append(" NUPROTOCOLOAUTORIZACAO,");
        sql.append(" DTAUTORIZACAO,");
        sql.append(" HRAUTORIZACAO,");
        sql.append(" VLTOTALTRIBUTOS,");
        sql.append(" VLPCTTRIBUTOSFEDERAIS,");
        sql.append(" VLPCTTRIBUTOSESTADUAIS,");
        sql.append(" VLPCTTRIBUTOSMUNICIPAIS,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" CDTIPOEMISSAO,");
        sql.append(" NUVERSAOQRCODE,");
        sql.append(" NUAMBIENTENFCE,");
        sql.append(" DSDIGESTVALUENFCE,");
        sql.append(" CDIDENTIFICACAOCSC,");
        sql.append(" DSURLQRCODE,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO,");
        sql.append(" QTTOTALITEM,");
        sql.append(" CDMENSAGEMRETORNO,");
        sql.append(" DSMENSAGEMRETORNO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" NUPEDIDO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" VLTOTALNFCE,");
        sql.append(" VLTOTALDESCONTO,");
        sql.append(" VLTOTALLIQUIDONFCE,");
        sql.append(" DSFORMAPAGAMENTO,");
        sql.append(" VLTOTALPAGO,");
        sql.append(" VLTROCO,");
        sql.append(" NUCHAVEACESSO,");
        sql.append(" NUNFCE,");
        sql.append(" NUSERIE,");
        sql.append(" DTEMISSAO,");
        sql.append(" HREMISSAO,");
        sql.append(" NUPROTOCOLOAUTORIZACAO,");
        sql.append(" DTAUTORIZACAO,");
        sql.append(" HRAUTORIZACAO,");
        sql.append(" VLTOTALTRIBUTOS,");
        sql.append(" VLPCTTRIBUTOSFEDERAIS,");
        sql.append(" VLPCTTRIBUTOSESTADUAIS,");
        sql.append(" VLPCTTRIBUTOSMUNICIPAIS,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" CDTIPOEMISSAO,");
        sql.append(" NUVERSAOQRCODE,");
        sql.append(" NUAMBIENTENFCE,");
        sql.append(" DSDIGESTVALUENFCE,");
        sql.append(" CDIDENTIFICACAOCSC,");
        sql.append(" DSURLQRCODE,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO,");
        sql.append(" QTTOTALITEM,");
        sql.append(" CDMENSAGEMRETORNO,");
        sql.append(" DSMENSAGEMRETORNO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        Nfce nfce = (Nfce) domain;
        sql.append(Sql.getValue(nfce.cdEmpresa)).append(",");
        sql.append(Sql.getValue(nfce.cdRepresentante)).append(",");
        sql.append(Sql.getValue(nfce.nuPedido)).append(",");
        sql.append(Sql.getValue(nfce.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(nfce.vlTotalNfce)).append(",");
        sql.append(Sql.getValue(nfce.vlTotalDesconto)).append(",");
        sql.append(Sql.getValue(nfce.vlTotalLiquidoNfce)).append(",");
        sql.append(Sql.getValue(nfce.dsFormaPagamento)).append(",");
        sql.append(Sql.getValue(nfce.vlTotalPago)).append(",");
        sql.append(Sql.getValue(nfce.vlTroco)).append(",");
        sql.append(Sql.getValue(nfce.nuChaveAcesso)).append(",");
        sql.append(Sql.getValue(nfce.nuNfce)).append(",");
        sql.append(Sql.getValue(nfce.nuSerie)).append(",");
        sql.append(Sql.getValue(nfce.dtEmissao)).append(",");
        sql.append(Sql.getValue(nfce.hrEmissao)).append(",");
        sql.append(Sql.getValue(nfce.nuProtocoloAutorizacao)).append(",");
        sql.append(Sql.getValue(nfce.dtAutorizacao)).append(",");
        sql.append(Sql.getValue(nfce.hrAutorizacao)).append(",");
        sql.append(Sql.getValue(nfce.vlTotalTributos)).append(",");
        sql.append(Sql.getValue(nfce.vlPctTributosFederais)).append(",");
        sql.append(Sql.getValue(nfce.vlPctTributosEstaduais)).append(",");
        sql.append(Sql.getValue(nfce.vlPctTributosMunicipais)).append(",");
        sql.append(Sql.getValue(nfce.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(nfce.dtAlteracao)).append(",");
        sql.append(Sql.getValue(nfce.hrAlteracao)).append(",");
        sql.append(Sql.getValue(nfce.cdTipoEmissao)).append(",");
        sql.append(Sql.getValue(nfce.nuVersaoQrCode)).append(",");
        sql.append(Sql.getValue(nfce.nuAmbienteNfce)).append(",");
        sql.append(Sql.getValue(nfce.dsDigestValueNfce)).append(",");
        sql.append(Sql.getValue(nfce.cdIdentificacaoCsc)).append(",");
        sql.append(Sql.getValue(nfce.dsUrlQrCode)).append(",");
        sql.append(Sql.getValue(nfce.nuCarimbo)).append(",");
        sql.append(Sql.getValue(nfce.cdUsuario)).append(",");
        sql.append(Sql.getValue(nfce.qtTotalItem)).append(",");
        sql.append(Sql.getValue(nfce.cdMensagemRetorno)).append(",");
        sql.append(Sql.getValue(nfce.dsMensagemRetorno));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        Nfce nfce = (Nfce) domain;
        sql.append(" VLTOTALNFCE = ").append(Sql.getValue(nfce.vlTotalNfce)).append(",");
        sql.append(" VLTOTALDESCONTO = ").append(Sql.getValue(nfce.vlTotalDesconto)).append(",");
        sql.append(" VLTOTALLIQUIDONFCE = ").append(Sql.getValue(nfce.vlTotalLiquidoNfce)).append(",");
        sql.append(" DSFORMAPAGAMENTO = ").append(Sql.getValue(nfce.dsFormaPagamento)).append(",");
        sql.append(" VLTOTALPAGO = ").append(Sql.getValue(nfce.vlTotalPago)).append(",");
        sql.append(" VLTROCO = ").append(Sql.getValue(nfce.vlTroco)).append(",");
        sql.append(" NUCHAVEACESSO = ").append(Sql.getValue(nfce.nuChaveAcesso)).append(",");
        sql.append(" NUNFCE = ").append(Sql.getValue(nfce.nuNfce)).append(",");
        sql.append(" NUSERIE = ").append(Sql.getValue(nfce.nuSerie)).append(",");
        sql.append(" DTEMISSAO = ").append(Sql.getValue(nfce.dtEmissao)).append(",");
        sql.append(" HREMISSAO = ").append(Sql.getValue(nfce.hrEmissao)).append(",");
        sql.append(" NUPROTOCOLOAUTORIZACAO = ").append(Sql.getValue(nfce.nuProtocoloAutorizacao)).append(",");
        sql.append(" DTAUTORIZACAO = ").append(Sql.getValue(nfce.dtAutorizacao)).append(",");
        sql.append(" HRAUTORIZACAO = ").append(Sql.getValue(nfce.hrAutorizacao)).append(",");
        sql.append(" VLTOTALTRIBUTOS = ").append(Sql.getValue(nfce.vlTotalTributos)).append(",");
        sql.append(" VLPCTTRIBUTOSFEDERAIS = ").append(Sql.getValue(nfce.vlPctTributosFederais)).append(",");
        sql.append(" VLPCTTRIBUTOSESTADUAIS = ").append(Sql.getValue(nfce.vlPctTributosEstaduais)).append(",");
        sql.append(" VLPCTTRIBUTOSMUNICIPAIS = ").append(Sql.getValue(nfce.vlPctTributosMunicipais)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(nfce.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(nfce.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(nfce.hrAlteracao)).append(",");
        sql.append(" CDTIPOEMISSAO = ").append(Sql.getValue(nfce.cdTipoEmissao)).append(",");
        sql.append(" NUVERSAOQRCODE = ").append(Sql.getValue(nfce.nuVersaoQrCode)).append(",");
        sql.append(" NUAMBIENTENFCE = ").append(Sql.getValue(nfce.nuAmbienteNfce)).append(",");
        sql.append(" DSDIGESTVALUENFCE = ").append(Sql.getValue(nfce.dsDigestValueNfce)).append(",");
        sql.append(" CDIDENTIFICACAOCSC = ").append(Sql.getValue(nfce.cdIdentificacaoCsc)).append(",");
        sql.append(" DSURLQRCODE = ").append(Sql.getValue(nfce.dsUrlQrCode)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(nfce.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(nfce.cdUsuario)).append(",");
        sql.append(" QTTOTALITEM = ").append(Sql.getValue(nfce.qtTotalItem)).append(",");
        sql.append(" CDMENSAGEMRETORNO = ").append(Sql.getValue(nfce.cdMensagemRetorno)).append(",");
        sql.append(" DSMENSAGEMRETORNO = ").append(Sql.getValue(nfce.dsMensagemRetorno));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        Nfce nfce = (Nfce) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", nfce.cdEmpresa);
		if (ValueUtil.isEmpty(nfce.cdRepresentante) && ValueUtil.isNotEmpty(nfce.cdRepresentanteSupList)) {
			sqlWhereClause.addAndConditionIn("CDREPRESENTANTE", nfce.cdRepresentanteSupList);
		} else {
			sqlWhereClause.addAndConditionEquals("CDREPRESENTANTE", nfce.cdRepresentante);
		}
		sqlWhereClause.addAndCondition("NUPEDIDO = ", nfce.nuPedido);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", nfce.flOrigemPedido);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}