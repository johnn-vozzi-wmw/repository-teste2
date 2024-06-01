package br.com.wmw.lavenderepda.business.domain;


import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import totalcross.util.Date;
import totalcross.util.Vector;


public class NovoCliente extends BasePersonDomain {

	public static String TABLE_NAME = "TBLVPNOVOCLIENTE";
	public static final String APPOBJ_CAMPOS_FILTRO_NOVO_CLIENTE = "Novo Cliente";
	public static final String FLSTATUSCADASTRO_CONFIRMADO = "C";
	public static final String FLSTATUSCADASTRO_PENDENTE = "P";
	public static final String FLTIPOCADASTRO_PROSPECT = "P";

    public static String NOME_ENTIDADE = "NOVOCLIENTE";

    public static String NMCOLUNA_CDLATITUDE = "cdlatitude";
    public static String NMCOLUNA_CDLONGITUDE = "cdlongitude";
    public static String NMCOLUNA_FLCADCOORDENADALIBERADO = "flcadcoordenadaliberado";
    public static String NMCOLUNA_DTCADASTRO = "dtCadastro";
    public static final String NMCOLUNA_HRCADASTRO = "hrcadastro";
    public static String NMCOLUNA_FLOCULTO = "flOculto";
    public static String NMCOLUNA_FLEMANALISE = "flEmAnalise";
    public static String NMCOLUNA_NMRAZAOSOCIAL = "nmRazaoSocial";
	public static String NMCOLUNA_CDUFCOMERCIAL = "cdufcomercial";
    public static String NMCOLUNA_DSNUMEROLOGRADOUROCOMERCIAL = "dsnumerologradourocomercial";
    public static final String NMCOLUNA_DSCEPCOMERCIAL = "dscepcomercial";
    public static final String NMCOLUNA_NUIERG = "nuierg";


	public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemNovoCliente;
    public String cdNovoCliente;
    public String nuCnpj;
    public String flTipoPessoa;
    public Date dtCadastro;
    public String hrCadastro;
    public String flTipoFrequencia;
    public String flSemanaMes;
    public Date dtBase;
    public String nuDiaSemana;
    public String hrAgenda;
    public String cdStatusNovoCliente;
    public String cdClienteOriginal;
    public double cdLatitude;
	public double cdLongitude;    
	public String flCadCoordenadaLiberado;    
	public String nmRazaoSocial;    
	public String nmFantasia;    
	public String flEmAnalise;
	public String flOculto;
	public String flStatusCadastro;
	public String cdUsuarioCriacao;
	public String flPrimeiraEtapa;

    //Não persistente
    public Vector novoCliEnderecoList;
    private Vector fotoNovoClienteList;
    private Vector fotoNovoClienteExcluidasList;
    public NovoClienteAna novoClienteAna;
    public Date dtCadastroInicial;
    public Date dtCadastroFinal;
    public boolean filtraNaoOcultos;
    public String flTipoCadastro;
    public String oldNuCnpj;
    public String oldCdRepresentante;
    public boolean filtraNaoEnviados;
    public boolean filtraStatusCadastro;
    public boolean salvaFlConsumidorFinal;
	public int qtContato;
	public boolean validaQtContato;
    
    public NovoCliente() {
    	super(TABLE_NAME);
    	novoCliEnderecoList = new Vector();
    	fotoNovoClienteList = new Vector();
    	fotoNovoClienteExcluidasList = new Vector();
    	novoClienteAna = new NovoClienteAna();
	}

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof NovoCliente) {
            NovoCliente novoCliente = (NovoCliente) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, novoCliente.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, novoCliente.cdRepresentante) &&
                ValueUtil.valueEquals(flOrigemNovoCliente, novoCliente.flOrigemNovoCliente) &&
                ValueUtil.valueEquals(cdNovoCliente, novoCliente.cdNovoCliente);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(flOrigemNovoCliente);
    	strBuffer.append(";");
    	strBuffer.append(cdNovoCliente);
        return strBuffer.toString();
    }

    public static String getPathImg() {
		return FotoUtil.getPathImg(NovoCliente.class);
	}

	public Vector getFotoNovoClienteList() {
		return fotoNovoClienteList;
	}

	public void setFotoNovoClienteList(Vector fotoNovoClienteList) {
		this.fotoNovoClienteList = fotoNovoClienteList;
	}

	public Vector getFotoNovoClienteExcluidasList() {
		return fotoNovoClienteExcluidasList;
	}

	public void setFotoNovoClienteExcluidasList(Vector fotoNovoClienteExcluidasList) {
		this.fotoNovoClienteExcluidasList = fotoNovoClienteExcluidasList;
	}
	
    public boolean isModoDeProspeccao() {
    	return LavenderePdaConfig.usaClienteEmModoProspeccao && Cliente.TIPO_PROSPECTS.equals(flTipoCadastro);
    }
    
    public NovoClienteAna toNovoClienteAna() {
    	NovoClienteAna novoClienteAna = new NovoClienteAna();
    	novoClienteAna.cdEmpresa = this.cdEmpresa;
    	novoClienteAna.cdRepresentante = this.cdRepresentante;
    	novoClienteAna.flOrigemNovoCliente = this.flOrigemNovoCliente;
    	novoClienteAna.cdNovoCliente = this.cdNovoCliente;
    	return novoClienteAna;
	}
    
    public boolean isPendente() {
    	return FLSTATUSCADASTRO_PENDENTE.equals(flStatusCadastro);
    }
    
    public boolean isConfirmado() {
    	return FLSTATUSCADASTRO_CONFIRMADO.equals(flStatusCadastro);
    }
    
    public boolean isPrimeiraEtapa() {
    	return ValueUtil.VALOR_SIM.equals(flPrimeiraEtapa);
    }
    
    public boolean isPessoaJuridica() {
    	return ValueUtil.valueEquals(Messages.TIPOPESSOA_FLAG_JURIDICA, flTipoPessoa);
    }

}
