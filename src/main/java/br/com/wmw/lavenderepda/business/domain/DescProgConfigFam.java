package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class DescProgConfigFam extends LavendereBasePersonDomain {

	public static final String TABLE_NAME = "TBLVPDESCPROGCONFIGFAM";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdDescProgressivo;
	public String cdFamiliaDescProg;
	public String flTipoFamiliaPro;
	public String flTipoFamiliaCon;
	public String flFamAcuValorMin;
	public String flFamAcuValorMax;
	public String flTipoAlteracao;

	// Não persistente
	public DescProgFamilia descProgFamilia;
	public int qtProdutos;
	public String flProduziuErp;
	public String flConsumiuErp;
	public String flAcumulouErp;
	public String flProduziuPda;
	public String flConsumiuPda;
	public String flAcumulouPda;
	public String flAcumulouMaxPda;
	public String flAcumulouMaxErp;
	public String flProduziuPdaOutros;
	public String flConsumiuPdaOutros;
	public String flAcumulouPdaOutros;
	public String flAcumulouMaxPdaOutros;
	public Pedido pedidoFilter;
	public String flStatusFilter;
	public Cliente cliente;

	public DescProgConfigFam() {
		super(TABLE_NAME);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DescProgConfigFam)) return false;
		DescProgConfigFam descProgressivoConfig = (DescProgConfigFam) obj;
		return ValueUtil.valueEquals(cdEmpresa, descProgressivoConfig.cdEmpresa)
				&& ValueUtil.valueEquals(cdRepresentante, descProgressivoConfig.cdRepresentante)
				&& ValueUtil.valueEquals(cdDescProgressivo, descProgressivoConfig.cdDescProgressivo)
				&& ValueUtil.valueEquals(cdFamiliaDescProg, descProgressivoConfig.cdFamiliaDescProg);
	}

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdRepresentante);
		primaryKey.append(";");
		primaryKey.append(cdDescProgressivo);
		primaryKey.append(";");
		primaryKey.append(cdFamiliaDescProg);
		return primaryKey.toString();
	}

	@Override
	public String getCdDomain() {
		return cdFamiliaDescProg;
	}

	@Override
	public String getDsDomain() {
		return descProgFamilia != null ? descProgFamilia.dsFamiliaProd : ValueUtil.VALOR_NI;
	}

	public boolean getFlProduziuErp() {
		return ValueUtil.getBooleanValue(flProduziuErp);
	}

	public boolean getFlConsumiuErp() {
		return ValueUtil.getBooleanValue(flConsumiuErp);
	}

	public boolean getFlAcumulouErp() {
		return ValueUtil.getBooleanValue(flAcumulouErp);
	}

	public boolean getFlProduziuPda() {
		return ValueUtil.getBooleanValue(flProduziuPda);
	}

	public boolean getFlConsumiuPda() {
		return ValueUtil.getBooleanValue(flConsumiuPda);
	}

	public boolean getFlAcumulouPda() {
		return ValueUtil.getBooleanValue(flAcumulouPda);
	}

	public boolean getFlAcumulouMaxPda() {
		return ValueUtil.getBooleanValue(flAcumulouMaxPda);
	}

	public boolean getFlAcumulouMaxErp() {
		return ValueUtil.getBooleanValue(flAcumulouMaxErp);
	}
	
	public boolean getFlProduziuPdaOutros() {
		return ValueUtil.getBooleanValue(flProduziuPdaOutros);
	}

	public boolean getFlConsumiuPdaOutros() {
		return ValueUtil.getBooleanValue(flConsumiuPdaOutros);
	}

	public boolean getFlAcumulouPdaOutros() {
		return ValueUtil.getBooleanValue(flAcumulouPdaOutros);
	}
	
	public boolean getFlAcumulouMaxPdaOutros() {
		return ValueUtil.getBooleanValue(flAcumulouMaxPdaOutros);
	}

	public boolean isFlTipoFamiliaPro() {
		return ValueUtil.getBooleanValue(flTipoFamiliaPro);
	}

	public boolean isFlTipoFamiliaCon() {
		return ValueUtil.getBooleanValue(flTipoFamiliaCon);
	}

	public boolean isFlFamAcuValorMin() {
		return ValueUtil.getBooleanValue(flFamAcuValorMin);
	}

	public boolean isFlFamAcuValorMax() {
		return ValueUtil.getBooleanValue(flFamAcuValorMax);
	}

}
