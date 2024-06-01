package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ConfigInterno extends BaseDomain {

    public static final String TABLE_NAME = "TBLVPCONFIGINTERNO";

	public static final char defaultSeparatorInfoValue = ':';

	public static final String CDEMPRESADEFAULT = "-1";
	public static final String VLCHAVEDEFAULT = "0";
	public static final String VLCHAVEAPP = "APP";
	public static final String VLCHAVEWEB = "WEB";
	
	// POR EMPRESA
	public static final int tempoUtimoEnvioPedidos = 2;
	public static final int dataHoraUtimoRecebimentoDados = 3;
    public static final int dataHoraUltimoReorganizarDados = 4;
    public static final int dataHoraUltimoBackup = 5;
    public static final int novaCargaCompleta = 6;
    public static final int dataPrimeiroRecebimentoDados = 9;
    public static final int dataUltimoAcessoSistema = 11;
    public static final int configLineGraphProperties = 12;
    public static final int configGraphLinesMetaAcompanhamento = 13;
    public static final int configGraphLinesVisitaAcompanhamento = 14;
    public static final int configSortAndOrderColumn = 15;
    public static final int clienteRedeConfig = 16;
    public static final int tecladoConfiguracoes = 19;
    public static final int ultimoRepEmpSelecionado = 20;
    public static final int configsPadraoMetaVenda = 21;
    public static final int temaPadraoSistema = 22;
    public static final int configPortaImpressora = 23;
    public static final int configEnderecoImpressora = 24;
    public static final int configNomeImpressora = 25;
    public static final int configSugestaoVendaSemQtde = 26;
    public static final int configSugestaoVendaComQtde = 27;
    public static final int configsPadraoMetaVendaCli = 28;
    public static final int configsPadraoMetaVendaCliTodos = 29;
    public static final int tipoMenuSistema = 30;
	public static final int tempoUltimaSugestaoEnvioPedidos = 32;
	public static final int CONFIG_USUARIO_ATIVO = 33;
	public static final int salvaDataHoraOriginalAgendaVisita = 34;
	public static final int dataPrimeiroAcessoAoSistema = 35;
	public static final int devolverEstoqueAtual = 38;
	public static final int dataUltimoCancelamentoAutomaticoPedido = 47;
	public static final int ULTIMACONFIGMODOFEIRA = 56;

    // GERAL
    public static final int dataHoraServidor = 7;
    public static final int dataHoraPda = 8;
    public static final int msgAlertaSistema = 18;
    public static final int nuVersaoAtual = 29;
    public static final int FUNCIONALIDADELIBERADASENHA = 36;
    public static final int apresentaApenasVerbaPresentePedido = 37;
    public static final int ULTIMOREPSELECBYSUPERVISOR = 39;
    public static final int DATAHORAULTIMAGERACAOALEATORIOS = 40;
    public static final int ULTIMOSNUMEROSALEATORIOSGERADOS = 41;
    public static final int QUANTIDADEEXECUCOES = 42;
    public static final int NAOHOUVEPRIMEIROACESSOMULTIPLASSUGESTOES = 43;
    public static final int FUSOHORARIO = 48;
    public static final int configTecladoSelecionado = 45;
    public static final int mensagemNovidadeTecladoNativo = 46;
    public static final int DATAPRIMEIROACESSOREPRESENTANTE = 49;
    public static final int CARGAINICIALFOTOSPENDENTE = 50;
    public static final int CARGADADOSAPPSEMUSUARIOPENDENTE = 51;
    public static final int ULTIMACONFIGURACAOBOTAOOCULTAR = 52;
    public static final int ULTIMACONFIGURACAOBOTAOAGRUPADORGRADE = 53;
	public static final int ULTIMOSTATUSEXCECAOSELECIONADO = 54;
    public static final int ULTIMADTHRATUALIZACAOFOTOMENUCATALOGO = 55;
    public static final int LIBERADOUTILIZACAOSENHACERTIFICADOIOS = 57;
	public static final int CONTROLEFALHALOGIN = 58;

	public String cdEmpresa;
    public int cdConfigInterno;
    public String vlChave;
    public String vlConfigInterno;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ConfigInterno) {
            ConfigInterno configInterno = (ConfigInterno) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, configInterno.cdEmpresa) &&
                ValueUtil.valueEquals(cdConfigInterno, configInterno.cdConfigInterno) &&
            	ValueUtil.valueEquals(vlChave, configInterno.vlChave);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdConfigInterno);
    	strBuffer.append(";");
    	strBuffer.append(vlChave);
        return strBuffer.toString();
    }

    @Override
    public boolean isValidateRowkey() {
    	return false;
    }
}