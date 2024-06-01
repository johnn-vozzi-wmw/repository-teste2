package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.interfaces.FileProperties;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import totalcross.json.JSONObject;
import totalcross.sys.Convert;
import totalcross.util.Date;

public class FotoClienteErp extends BaseDomain implements FileProperties {
	
	public static final String TABLE_NAME = "TBLVPFOTOCLIENTEERP";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdCliente;
    public String nmFoto;
    public String nmFotoRelacionada;
    public int cdFotoCliente;
    public int nuTamanho;
    public Date dtModificacao;
    public String flFotoExcluida;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof FotoClienteErp) {
        	FotoClienteErp fotoClientEerp = (FotoClienteErp) obj;
            return
                ValueUtil.valueEquals(cdCliente, fotoClientEerp.cdCliente) && 
                ValueUtil.valueEquals(nmFoto, fotoClientEerp.nmFoto);
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

	@Override
	public String getFileName() {
		return nmFoto;
	}

	@Override
	public String getAbsolutePath() {
		return Convert.appendPath(Cliente.getPathImg(), getFileName());
	}

	@Override
	public String getHrModificacao() {
		return null;
	}

	@Override
	public Date getDtModificacao() {
		return dtModificacao;
	}

	@Override
	public JSONObject getRequestJson() {
		JSONObject json = new JSONObject();
		json.put("cdEmpresa", cdEmpresa);
		json.put("cdRepresentante", cdRepresentante);
		json.put("cdCliente", cdCliente);
		json.put("fileName", nmFoto);
		return json;
	}

	@Override
	public String getHttpEndpoint() {
		return LavendereWeb2Tc.ACTION_GET_FOTO_CLIENTEERP;
	}

	@Override
	public String getNmCampoUpdateRecebimento() {
		return BaseDomain.NMCAMPOROWKEY;
	}

	@Override
	public String getVlCampoUpdateRecebimento() {
		return rowKey;
	}

}