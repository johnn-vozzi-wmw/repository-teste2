package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import totalcross.util.Date;

public class DescProgressivoConfig extends LavendereBasePersonDomain {

	public static final String TABLE_NAME = "TBLVPDESCPROGRESSIVOCONFIG";

    public static final int NU_NIVEL_CLIENTE_CLIENTE = 1;
    public static final int NU_NIVEL_CLIENTE_FAIXA = 2;

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdDescProgressivo;
    public int nuNivelCliente;
    public String dsDescProgressivo;
    public double vlMinDescProgressivo;
    public double vlMaxDescProgressivo;
    public Date dtInicialVigencia;
    public Date dtFimVigencia;
    public String flTipoAlteracao;

    // Não Persistentes
    public Cliente cliente;
    public double vlAtingido;
    public double vlAtingidoMax;
    public int qtAtingidoMax;
    public int qtAtingido;
	public Pedido pedidoFilter;
    public double vlAtingidoPedidoAtual;
    public double vlAtingidoPedidoAtualMax;
    public boolean atingiuVlMin;
    public boolean atingiuVlMax;

    public DescProgressivoConfig() { super(TABLE_NAME); }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DescProgressivoConfig)) return false;
        DescProgressivoConfig descProgressivoConfig = (DescProgressivoConfig) obj;
        return ValueUtil.valueEquals(cdEmpresa, descProgressivoConfig.cdEmpresa)
            && ValueUtil.valueEquals(cdRepresentante, descProgressivoConfig.cdRepresentante)
            && ValueUtil.valueEquals(cdDescProgressivo, descProgressivoConfig.cdDescProgressivo)
            && ValueUtil.valueEquals(nuNivelCliente, descProgressivoConfig.nuNivelCliente);
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdDescProgressivo);
        primaryKey.append(";");
        primaryKey.append(nuNivelCliente);
        return primaryKey.toString();
    }

    @Override public String getCdDomain() { return cdDescProgressivo; }
    @Override public String getDsDomain() { return dsDescProgressivo; }

    public boolean isNivelClienteCliente() { return nuNivelCliente == NU_NIVEL_CLIENTE_CLIENTE; }
    public boolean isNivelClienteFaixa() { return nuNivelCliente == NU_NIVEL_CLIENTE_FAIXA; }

    public boolean atingiuVlMinimo() {
    	return vlAtingido + vlAtingidoPedidoAtual >= vlMinDescProgressivo;
    }

    public boolean atingiuVlMaximo() {
        if (vlMaxDescProgressivo <= 0) return false;
        return vlAtingidoMax + vlAtingidoPedidoAtualMax > vlMaxDescProgressivo;
    }

}
