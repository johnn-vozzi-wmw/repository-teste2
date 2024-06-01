package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import totalcross.util.Date;
import totalcross.util.Vector;

public class PesquisaMercado extends BaseDomain {

    public static String TABLE_NAME = "TBLVPPESQUISAMERCADO";
    
    public static final String CDTIPOPESQUISA_VALOR = "1";
    public static final String CDTIPOPESQUISA_GONDOLA = "2";

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPesquisaMercado;
    public String cdPesquisaMercado;
    public String cdCliente;
    public String cdProduto;
    public String cdProdutoConcorrente;
    public String cdConcorrente;
    public Date dtEmissao;
    public double vlUnitario;
    public double qtItem;
    public String dsObservacao;
    public int qtItemFrente;
    public int qtItemProfundidade;
    public int qtItemAndar;
    public int qtItemTotal;
    public String cdTipoPesquisa;
    public String flPesquisaNovoCliente;
    public String cdUsuarioEmissao;
    public String nuPedido;
    public String flOrigemPedido;
    public Double cdLatitude;
    public Double cdLongitude; 

    //--
	public Vector fotoList;
	//--
	public boolean inInsertList;
	public boolean isLiberadoParaSalvar;

	public PesquisaMercado() {
		this.fotoList = new Vector(0);
	}
	
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof PesquisaMercado) {
            PesquisaMercado pesquisamercado = (PesquisaMercado) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, pesquisamercado.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, pesquisamercado.cdRepresentante) &&
                ValueUtil.valueEquals(flOrigemPesquisaMercado, pesquisamercado.flOrigemPesquisaMercado) &&
                ValueUtil.valueEquals(cdPesquisaMercado, pesquisamercado.cdPesquisaMercado);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(flOrigemPesquisaMercado);
    	strBuffer.append(";");
    	strBuffer.append(cdPesquisaMercado);
        return strBuffer.toString();
    }
    
    public boolean isTipoPesquisaValor() {
    	return CDTIPOPESQUISA_VALOR.equals(cdTipoPesquisa);
    }
    
    public boolean isTipoPesquisaGondola() {
    	return CDTIPOPESQUISA_GONDOLA.equals(cdTipoPesquisa);
    }
    
    public String getDsTipoPesquisa() {
    	if (isTipoPesquisaValor()) {
    		return Messages.PESQUISAMERCADO_PESQUISA_VALOR_ABR;
    	}
    	if (isTipoPesquisaGondola()) {
    		return Messages.PESQUISAMERCADO_PESQUISA_GONDOLA_ABR;
    	}
    	return ValueUtil.VALOR_NI;
    }
    
}