package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class Recado extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPRECADO";

	public static final int STATUSENVIO_CAIXASAIDA = 2;
	public static final int STATUSENVIO_ITENSENVIADOS = 3;

	public static int MAX_LENGTH_DS_RECADO = 4000;
	public static final String TIPORECADO_NORMAL = "1";
	public static final String TIPORECADO_RESPOSTA_NORMAL = "2";
	public static final String TIPORECADO_RESPOSTA_OBRIGATORIO = "3";

	public String cdRecado;
    public String cdUsuarioRemetente;
    public String cdUsuarioDestinatario;
    public String dsAssunto;
    public String dsRecado;
    public Date dtCadastro;
    public String hrCadastro;
    public Date dtEnvio;
    public String hrEnvio;
    public Date dtLeitura;
    public String hrLeitura;
    public String flLido;
    public String cdStatusEnvio;
    public String flObrigaResposta;
    public String flRespostaEnviada;
    public String flTipoRecado;
    public String cdRecadoRelacionado;
    public String cdEmpresaCliente;
    public String cdRepresentanteCliente;
    public String cdCliente;
    
    //Nao Persistentes
    public Empresa empresa;
    public Cliente cliente;

    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Recado) {
            Recado recado = (Recado) obj;
            return
                ValueUtil.valueEquals(cdRecado, recado.cdRecado);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
    	return  cdRecado;
    }

    public boolean isRecadoResposta() {
		return !Recado.TIPORECADO_NORMAL.equals(flTipoRecado);
	}

    public boolean isObrigaResposta() {
    	return ValueUtil.VALOR_SIM.equals(flObrigaResposta);
    }

}