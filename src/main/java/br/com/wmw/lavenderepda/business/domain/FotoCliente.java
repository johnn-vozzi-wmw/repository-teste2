package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class FotoCliente extends BaseDomain {

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdCliente;
    public String nmFoto;
    public String nmFotoRelacionada;
    public int cdFotoCliente;
    public int nuTamanho;
    public Date dtModificacao;
    public String flFotoExcluida;
    
    //Não persistente
    public Date dtModificacaoFilter;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof FotoCliente) {
            FotoCliente fotoCliente = (FotoCliente) obj;
            return
                ValueUtil.valueEquals(cdCliente, fotoCliente.cdCliente) && 
                ValueUtil.valueEquals(nmFoto, fotoCliente.nmFoto);
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
        primaryKey.append(nmFoto);
        return primaryKey.toString();
    }

}