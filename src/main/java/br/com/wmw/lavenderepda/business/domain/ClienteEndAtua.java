package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import totalcross.util.Date;

public class ClienteEndAtua extends BasePersonDomain {
	
	public static String TABLE_NAME = "TBLVPCLIENTEENDATUA";
	public static final String CDENDERECO_NOVO_CADASTRO = "0";
	public static final String FLTIPOREGISTRO_NOVO = "N";
    public static final String FLTIPOREGISTRO_ALTERACAO = "A";
    public static final String FLTIPOREGISTRO_EXCLUSAO = "E";
	
	public static final String FLORIGEM_PDA = "P";

	public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdEndereco;
    public String cdRegistro;
    public String flOrigemAtualizacao;
    public String dsLogradouro;
    public String nuLogradouro;
    public String dsComplemento;
    public String dsBairro;
    public String dsCidade;
    public String dsEstado;
    public String dsPais;
    public String dsCep;
    public Date dtAtualizacao;
    public String flTipoRegistro;
    public String cdPeriodoEntrega;
    public String cdUsuarioAlteracao;
    
    //Não persistente
    public Date dtAtualizacaoMenorIgualFilter;
    public String flEntrega;
    public String flCobranca;
	public String dsDiaEntrega;
    
    
    public ClienteEndAtua() {
    	super(TABLE_NAME);
    }

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ClienteEndAtua) {
            ClienteEndAtua clienteEndAtua = (ClienteEndAtua) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, clienteEndAtua.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, clienteEndAtua.cdRepresentante) && 
                ValueUtil.valueEquals(cdCliente, clienteEndAtua.cdCliente) && 
                ValueUtil.valueEquals(cdEndereco, clienteEndAtua.cdEndereco) && 
                ValueUtil.valueEquals(cdRegistro, clienteEndAtua.cdRegistro) &&
                ValueUtil.valueEquals(flOrigemAtualizacao, clienteEndAtua.flOrigemAtualizacao);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdEndereco);
        primaryKey.append(";");
        primaryKey.append(cdRegistro);
        primaryKey.append(";");
        primaryKey.append(flOrigemAtualizacao);
        return primaryKey.toString();
    }
    
    public boolean isEnderecoExcluido() {
    	return FLTIPOREGISTRO_EXCLUSAO.equals(flTipoRegistro);
    }
    
    public boolean isNovoEndereco() {
    	return FLTIPOREGISTRO_NOVO.equals(flTipoRegistro);
    }
    
    public boolean isEdicaoEndereco() {
    	return !isEnderecoExcluido() && !isNovoEndereco();
    }
    
    public String getTipoAtualizacao() {
    	if (isEnderecoExcluido()) {
			return Messages.CLIENTEENDATUA_LABEL_TIPOATUALIZACAO_EXCLUSAO;
		} else if (isNovoEndereco()) {
			return Messages.CLIENTEENDATUA_LABEL_TIPOATUALIZACAO_INCLUSAO;
		} else {
			return Messages.CLIENTEENDATUA_LABEL_TIPOATUALIZACAO_EDICAO;
		}
    }

}