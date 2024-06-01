package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class FotoProspect extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPFOTOPROSPECT";

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemProspect;
    public String cdProspect;
    public String nmFoto;
    public int nuTamanho;
    public Date dtModificacao;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FotoProspect) {
            FotoProspect fotoProspect = (FotoProspect) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, fotoProspect.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, fotoProspect.cdRepresentante) && 
                ValueUtil.valueEquals(flOrigemProspect, fotoProspect.flOrigemProspect) && 
                ValueUtil.valueEquals(cdProspect, fotoProspect.cdProspect) && 
                ValueUtil.valueEquals(nmFoto, fotoProspect.nmFoto);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(flOrigemProspect);
        primaryKey.append(";");
        primaryKey.append(cdProspect);
        primaryKey.append(";");
        primaryKey.append(nmFoto);
        return primaryKey.toString();
    }
    
    public String getNmFotoSemExtensao() {
    	return nmFoto.substring(0, nmFoto.lastIndexOf("."));
    }
    
    public String getExtensao() {
    	return nmFoto.substring(nmFoto.lastIndexOf("."));
    }

}