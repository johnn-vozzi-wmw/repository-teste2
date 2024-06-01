package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class Marcador extends LavendereBaseDomain {

	public static final String TABLE_NAME = "TBLVPMARCADOR";
	public static final String ENTIDADE_MARCADOR_CLIENTE = "CLIENTE";
	public static final String ENTIDADE_MARCADOR_PEDIDO = "PEDIDO";
	public static final String ENTIDADE_MARCADOR_PRODUTO = "PRODUTO";

    public String cdMarcador;
    public String dsMarcador;
    public String nmEntidade;
    public Date dtInicioVigencia;
    public Date dtTerminovigencia;
    public byte[] imMarcadorAtivo;
    public int nuSequencia;
    public int cdTipomarcador;
    public String flFiltroSelecionado;
    public String flAgrupadorGrade;
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Marcador) {
            Marcador marcador = (Marcador) obj;
            return ValueUtil.valueEquals(cdMarcador, marcador.cdMarcador);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
	    return cdMarcador;
    }

	@Override
	public String getCdDomain() {
		return cdMarcador;
	}

	@Override
	public String getDsDomain() {
		return dsMarcador;
	}

	@Override
	public String getSortStringValue() {
		/* Utilizado para ordenação da sequencia de marcadores escolhidos na combo MarcadorMultiComboBox */
		return nuSequencia + "-" + dsMarcador;
	}

	public boolean isFiltroSelecionadoPorPadrao() {
        return ValueUtil.valueEquals(flFiltroSelecionado, ValueUtil.VALOR_SIM);
    }

}