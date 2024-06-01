package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class EnderecoGpsPda extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPENDERECOGPSPDA";

    public String dsBairro;
    public String dsCidade;
    public String dsEstado;
    public String dsLogradouro;
    public String dsCep;
    public String nuLogradouro;
    public double cdLatitude;
    public double cdLongitude;
    public Date dtColeta;
    public String hrColeta;
    public String cdCliente;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof EnderecoGpsPda) {
            EnderecoGpsPda enderecoGpsPda = (EnderecoGpsPda) obj;
            return
                ValueUtil.valueEquals(dsBairro, enderecoGpsPda.dsBairro) && 
                ValueUtil.valueEquals(dsCidade, enderecoGpsPda.dsCidade) && 
                ValueUtil.valueEquals(dsEstado, enderecoGpsPda.dsEstado) && 
                ValueUtil.valueEquals(dsLogradouro, enderecoGpsPda.dsLogradouro) && 
                ValueUtil.valueEquals(dsCep, enderecoGpsPda.dsCep) && 
                ValueUtil.valueEquals(nuLogradouro, enderecoGpsPda.nuLogradouro);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(dsBairro);
        primaryKey.append(";");
        primaryKey.append(dsCidade);
        primaryKey.append(";");
        primaryKey.append(dsEstado);
        primaryKey.append(";");
        primaryKey.append(dsLogradouro);
        primaryKey.append(";");
        primaryKey.append(dsCep);
        primaryKey.append(";");
        primaryKey.append(nuLogradouro);
        return primaryKey.toString();
    }

}