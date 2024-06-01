package br.com.wmw.lavenderepda.business.domain;

public class CampanhaPublicitaria extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPENVIOCAMPANHAPUB";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdCampanhaPublicitaria;
	public String cdCliente;
	public String dsCampanhaPublicitaria;

	@Override
	public String getCdDomain() {
		return cdCampanhaPublicitaria;
	}

	@Override
	public String getDsDomain() {
		return dsCampanhaPublicitaria;
	}

	@Override
	public String getPrimaryKey() {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append(cdEmpresa);
		strBuffer.append(";");
		strBuffer.append(cdRepresentante);
		strBuffer.append(";");
		strBuffer.append(cdCampanhaPublicitaria);
		strBuffer.append(";");
		strBuffer.append(cdCliente);
		return strBuffer.toString();
	}
}
