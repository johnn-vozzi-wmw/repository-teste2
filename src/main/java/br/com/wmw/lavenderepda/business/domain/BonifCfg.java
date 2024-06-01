package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import totalcross.util.Date;

public class BonifCfg extends LavendereBaseDomain {
	
	public static String TABLE_NAME = "TBLVPBONIFCFG";
	public static final String CDTIPOREGRA_VALOR = "1";
	public static final String CDTIPOREGRA_QTDE = "2";
	public static final String CDTIPOREGRA_CONTACORRENTE = "3";
	
	public String cdEmpresa;
    public String cdBonifCfg;
    public String dsBonifCfg;
    public Date dtVigenciaIni;
    public Date dtVigenciaFim;
    public String flOpcional;
    public double vlPctSobreVenda;
    public int nuPrioridade;
    public String cdTipoRegraBonificacao;
    public String flBonificacaoAutomatica;
    public double qtSaldoContaCorrente;
    
    //Nao persistentes
    private BonifCfgRepresentante bonCfgRep;
    private BonifCfgCategoria bonCfgCat;
    private BonifCfgLinha bonCfgLinha;
    private BonifCfgFamilia bonCfgFam;
    private BonifCfgProduto bonifCfgProduto;
    private BonifCfgConjunto bonifCfgConjunto;
    private BonifCfgCliente bonifCfgCliente;
	public double sumVlBonificacao;
	public double sumQtBonificacao;
	public boolean primeiroLista;

	public BonifCfg() {
		super();
	}
	
	public BonifCfg(String cdEmpresa, String cdBonifCfg) {
		this.cdEmpresa = cdEmpresa;
		this.cdBonifCfg = cdBonifCfg;
	}
	
	public BonifCfg(ItemPedidoBonifCfg itemPedBonCfg) {
		this.cdEmpresa = itemPedBonCfg.cdEmpresa;
		this.cdBonifCfg = itemPedBonCfg.cdBonifCfg;
		this.cdUsuario = itemPedBonCfg.cdUsuario;
	}
	
	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdBonifCfg);
        primaryKey.append(";");
        return primaryKey.toString();
	}
	
	@Override
	public String getCdDomain() {
		return this.cdBonifCfg;
	}

	@Override
	public String getDsDomain() {
		return this.dsBonifCfg;
	}

	public boolean isBonifCfgValorSobreVenda() {
		return this.vlPctSobreVenda > 0;
	}
	
	public boolean isOpcional() {
		return ValueUtil.getBooleanValue(this.flOpcional);
	}
	
	public BonifCfgRepresentante getBonCfgRep() {
		return bonCfgRep;
	}

	public void setBonCfgRep(BonifCfgRepresentante bonCfgRep) {
		this.bonCfgRep = bonCfgRep;
	}

	public BonifCfgCategoria getBonCfgCat() {
		return bonCfgCat;
	}

	public void setBonCfgCat(BonifCfgCategoria bonCfgCat) {
		this.bonCfgCat = bonCfgCat;
	}

	public BonifCfgLinha getBonCfgLinha() {
		return bonCfgLinha;
	}

	public void setBonCfgLinha(BonifCfgLinha bonCfgLinha) {
		this.bonCfgLinha = bonCfgLinha;
	}

	public BonifCfgFamilia getBonCfgFam() {
		return bonCfgFam;
	}

	public void setBonCfgFam(BonifCfgFamilia bonCfgFam) {
		this.bonCfgFam = bonCfgFam;
	}

	public boolean isTipoRegraQuantidade() {
		return CDTIPOREGRA_QTDE.equals(cdTipoRegraBonificacao) && LavenderePdaConfig.isUsaRegraFaixaQuantidade();
	}

	public boolean isTipoRegraValor() {
		return CDTIPOREGRA_VALOR.equals(cdTipoRegraBonificacao) && LavenderePdaConfig.isUsaRegraValorVenda();
	}
	
	public boolean isTipoRegraContaCorrente() {
		return CDTIPOREGRA_CONTACORRENTE.equals(cdTipoRegraBonificacao) && LavenderePdaConfig.isUsaRegraContaCorrenteQuantidade();
	}

	public boolean isBonificacaoAutomatica() {
		return ValueUtil.VALOR_SIM.equalsIgnoreCase(flBonificacaoAutomatica);
	}

	public BonifCfgProduto getBonifCfgProduto() {
		return bonifCfgProduto;
	}

	public void setBonifCfgProduto(BonifCfgProduto bonifCfgProduto) {
		this.bonifCfgProduto = bonifCfgProduto;
	}

	public BonifCfgConjunto getBonifCfgConjunto() {
		return bonifCfgConjunto;
	}

	public void setBonifCfgConjunto(BonifCfgConjunto bonifCfgConjunto) {
		this.bonifCfgConjunto = bonifCfgConjunto;
	}

	public BonifCfgCliente getBonifCfgCliente() {
		return bonifCfgCliente;
	}

	public void setBonifCfgCliente(BonifCfgCliente bonifCfgCliente) {
		this.bonifCfgCliente = bonifCfgCliente;
	}

}
