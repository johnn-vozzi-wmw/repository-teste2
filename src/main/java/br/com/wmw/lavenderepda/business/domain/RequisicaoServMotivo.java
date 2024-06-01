package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;

public class RequisicaoServMotivo extends LavendereBaseDomain {
	
    public static String TABLE_NAME = "TBLVPREQUISICAOSERVMOTIVO";
    
	public String cdRequisicaoServMotivo;
	public String dsRequisicaoServMotivo;
	public String flUsoMotivo;
	public String flObrigaObservacao;
	public String flStatusRequisicao;
	
	@Override
	public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdRequisicaoServMotivo);
        return primaryKey.toString();
	}
	
	public String getDsStatusRequisicao() {
		if (ValueUtil.isEmpty(flStatusRequisicao)) return ValueUtil.VALOR_NI;
		if (LavenderePdaConfig.usaDescricaoCodigoNaVisualizacaoEntidades) {
			switch (flStatusRequisicao) {
			case StatusRequisicaoServ.FLSTATUSPENDENTE:
				return Messages.REQUISICAOSERV_DSSTATUSPENDENTE+" ["+flStatusRequisicao+"]";
			case StatusRequisicaoServ.FLSTATUSANDAMENTO:
				return Messages.REQUISICAOSERV_DSSTATUSANDAMENTO+" ["+flStatusRequisicao+"]";
			case StatusRequisicaoServ.FLSTATUSENCERRADO:
				return Messages.REQUISICAOSERV_DSSTATUSENCERRADO+" ["+flStatusRequisicao+"]";
			case StatusRequisicaoServ.FLSTATUSCANCELADO:
				return Messages.REQUISICAOSERV_DSSTATUSCANCELADO+" ["+flStatusRequisicao+"]";
			default:
				return ValueUtil.VALOR_NI;					
			}	
		} else {
			switch (flStatusRequisicao) {
			case StatusRequisicaoServ.FLSTATUSPENDENTE:
				return Messages.REQUISICAOSERV_DSSTATUSPENDENTE;
			case StatusRequisicaoServ.FLSTATUSANDAMENTO:
				return Messages.REQUISICAOSERV_DSSTATUSANDAMENTO;
			case StatusRequisicaoServ.FLSTATUSENCERRADO:
				return Messages.REQUISICAOSERV_DSSTATUSENCERRADO;
			case StatusRequisicaoServ.FLSTATUSCANCELADO:
				return Messages.REQUISICAOSERV_DSSTATUSCANCELADO;
			default:
				return ValueUtil.VALOR_NI;					
			}						
		}
	}
	

	@Override
	public String getCdDomain() {
		return cdRequisicaoServMotivo;
	}

	@Override
	public String getDsDomain() {
		return dsRequisicaoServMotivo;
	}

}
