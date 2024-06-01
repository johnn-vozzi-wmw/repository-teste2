package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.json.JSONObject;

public class FotoProdutoEmp extends FotoProduto {
	
	public static final String TABLE_NAME = "TBLVPFOTOPRODUTOEMP";
	public static final String NMCOLUNA_CDEMPRESA = "CDEMPRESA";

	public String cdEmpresa;
	
	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdProduto + ";" + nmFoto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FotoProdutoEmp) {
			FotoProdutoEmp fotoProdutoEmp = (FotoProdutoEmp) obj;
			return ValueUtil.valueEquals(cdEmpresa, fotoProdutoEmp.cdEmpresa)
				&& ValueUtil.valueEquals(cdProduto, fotoProdutoEmp.cdProduto)
				&& ValueUtil.valueEquals(nmFoto, fotoProdutoEmp.nmFoto);
		}
		return false;
	}
	
	@Override
	public String getKeyHashFotoProduto() {
		return cdEmpresa + "/" + nmFoto;
	}
	
	@Override
	public String getFileName() {
		return cdEmpresa + "/" + nmFoto;
	}
	
	@Override
	public JSONObject getRequestJson() {
		JSONObject json = super.getRequestJson();
		json.put("cdEmpresa", cdEmpresa);
		return json;
	}

}
