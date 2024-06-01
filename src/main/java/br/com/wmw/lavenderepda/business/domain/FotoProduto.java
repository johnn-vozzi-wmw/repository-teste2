package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.interfaces.FileProperties;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import totalcross.json.JSONObject;
import totalcross.sys.Convert;
import totalcross.util.Date;

public class FotoProduto extends BaseDomain implements FileProperties {

    public static final String TABLE_NAME = "TBLVPFOTOPRODUTO";
    public static final String NM_COLUNA_NMFOTO = "nmFoto";

    public String cdProduto;
    public String nmFoto;
    public int nuTamanho;
    public Date dtModificacao;
    public String hrModificacao;
    
    //Nao persistente
    public String dsAgrupadorGrade;
    public String cdItemGrade1;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FotoProduto) {
            FotoProduto fotoProduto = (FotoProduto) obj;
            return ValueUtil.valueEquals(cdProduto, fotoProduto.cdProduto)
                && ValueUtil.valueEquals(nmFoto, fotoProduto.nmFoto);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(nmFoto);
        return primaryKey.toString();
    }
    
    public String getKeyHashFotoProduto() {
    	return nmFoto;
    }
    
    @Override
    public String getFileName() {
    	return nmFoto;
    }

	@Override
	public String getAbsolutePath() {
		return Convert.appendPath(Produto.getPathImg(), getFileName());
	}

	@Override
	public String getHrModificacao() {
		return hrModificacao;
	}

	@Override
	public Date getDtModificacao() {
		return dtModificacao;
	}
	
	@Override
	public JSONObject getRequestJson() {
		JSONObject json = new JSONObject();
		json.put("cdProduto", cdProduto);
		json.put("fileName", nmFoto);
		return json;
	}

	@Override
	public String getHttpEndpoint() {
		return LavendereWeb2Tc.ACTION_GET_FOTO_PRODUTO;
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