package br.com.wmw.lavenderepda.util;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.FileChooserBoxUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.framework.util.enums.GroupTypeFile;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DocumentoAnexo;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.framework.util.TypeFileUtil;
import br.com.wmw.framework.util.enums.TypeFile;
import br.com.wmw.lavenderepda.business.service.DocumentoAnexoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.ImageSliderDocumentoAnexoWindow;
import totalcross.io.File;
import totalcross.sys.Settings;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.util.Vector;

public class LavendereFileChooserBoxUtil extends FileChooserBoxUtil {

	private String nmEntidade;
	private String vlChave;
	private boolean setDadosAlteracao;
	private boolean insertFile;
	private DocumentoAnexo documentoAnexo;
	private Vector docAnexoList;
	private BaseDomain domain;
	
	public LavendereFileChooserBoxUtil(TypeFile[] fileFormats, String nmEntidade, BaseDomain domain, boolean setDadosAlteracao) {
		this(fileFormats, nmEntidade, domain, setDadosAlteracao, true, null);
	}

	public LavendereFileChooserBoxUtil(GroupTypeFile groupTypeFile, String nmEntidade, BaseDomain domain, boolean setDadosAlteracao) {
		this(groupTypeFile, nmEntidade, domain, setDadosAlteracao, true, null);
	}

	public LavendereFileChooserBoxUtil(TypeFile[] fileFormats, String nmEntidade, BaseDomain domain, boolean setDadosAlteracao, boolean insertFile, Vector docAnexoList) {
		super(fileFormats);
		init(nmEntidade, domain, setDadosAlteracao, insertFile, docAnexoList);
	}

	public LavendereFileChooserBoxUtil(GroupTypeFile groupTypeFile, String nmEntidade, BaseDomain domain, boolean setDadosAlteracao, boolean insertFile, Vector docAnexoList) {
		super(groupTypeFile);
		init(nmEntidade, domain, setDadosAlteracao, insertFile, docAnexoList);
	}

	private void init(String nmEntidade, BaseDomain domain, boolean setDadosAlteracao, boolean insertFile, Vector docAnexoList) {
		this.nmEntidade = nmEntidade;
		this.vlChave = domain.getRowKey();
		this.setDadosAlteracao = setDadosAlteracao;
		this.insertFile = insertFile;
		this.docAnexoList = docAnexoList;
		this.domain = domain;
	}
	
	@Override
	public void unpop() {
		super.unpop();
		String dsPath = getAnswer();
		if (dsPath != null && !dsPath.isEmpty()) {
			try {
				File file = new File(dsPath);
				if (file.isDir()) {
					UiUtil.showErrorMessage(Messages.BOTAO_ANEXAR_DOC_ERRO_DIR);
					return;
				}
			} catch (Throwable e) {
				UiUtil.showErrorMessage(Messages.BOTAO_ANEXAR_DOC_ERRO);
			}
			int index = dsPath.lastIndexOf("/") + 1;
			String nmArquivo = dsPath.substring(index, dsPath.length());
			if (UiUtil.showConfirmYesNoMessage(MessageUtil.getMessage(Messages.BOTAO_ANEXAR_DOC_CONFIRMACAO, nmArquivo))) {
				nmArquivo = StringUtil.changeStringAccented(nmArquivo);
				try {
					documentoAnexo = DocumentoAnexoService.getInstance().anexarDocumento(nmEntidade, vlChave, dsPath, nmArquivo, setDadosAlteracao, insertFile);
					if (documentoAnexo != null && !insertFile) {
						docAnexoList.addElement(documentoAnexo);
					}
					UiUtil.showInfoMessage(Messages.BOTAO_ANEXAR_DOC_SUCESSO);
				} catch (ValidationException | SQLException e) {
					UiUtil.showErrorMessage(e);
				}
			}
		}
		ListDocumentoAnexoWindow listDocumentoAnexoWindow = new ListDocumentoAnexoWindow();
		listDocumentoAnexoWindow.popup();
	}

	public static String getPathDoc(BaseDomain domain) {
		if (VmUtil.isWinCEPocketPc()) {
			return VmUtil.getWinCePocketCardDir() + getDirArquivoDomain(domain);
		} else if (VmUtil.isAndroid()) {
			return "/sdcard/" + getDirArquivoDomain(domain);
		} else {
			return Settings.appPath + "/" + getDirArquivoDomain(domain);
		}
	}
	
	private static String getDirArquivoDomain(BaseDomain domain) {
		if (domain == null) {
			return BaseDomain.getDirBaseArquivosInCard() + "/";
		}
		return BaseDomain.getDirBaseArquivosInCard() + domain.getClass().getSimpleName() + "/";
	}
	
	public void showListDocumentoAnexo() {
		ListDocumentoAnexoWindow listDocumentoAnexoWindow = new ListDocumentoAnexoWindow();
		listDocumentoAnexoWindow.popup();
	}
	
	public class ListDocumentoAnexoWindow extends WmwListWindow {
		
		private ButtonPopup btDelete;
		private ButtonPopup btNovo;
		
		public ListDocumentoAnexoWindow() {
			super(Messages.LISTA_ANEXOS);
			listContainer = new GridListContainer(1, 1);
			listContainer.setBarTopSimple();
			listContainer.setCheckable(true);
			btDelete = new ButtonPopup(Messages.BOTAO_DELETAR_ANEXO);
			btNovo = new ButtonPopup(Messages.BOTAO_NOVO_ANEXO);
			setDefaultRect();
		}

		@Override
		protected Vector getDomainList(BaseDomain domain) throws SQLException {
			if (ValueUtil.isNotEmpty(docAnexoList)) {
				return docAnexoList;
			}
			return getCrudService().findAllByExample(domain);
		}

		@Override
		protected String[] getItem(Object domain) {
			DocumentoAnexo documentoAnexo = (DocumentoAnexo) domain;
			String[] item = {StringUtil.getStringValue(documentoAnexo.nmArquivo)};
			return item;
		}

		@Override
		protected String getSelectedRowKey() {
			BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
			return c.id;
		}

		@Override
		protected CrudService getCrudService() {
			return DocumentoAnexoService.getInstance();
		}

		@Override
		protected BaseDomain getDomainFilter() {
			DocumentoAnexo documentoAnexoFilter = new DocumentoAnexo();
			documentoAnexoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			if (LavendereFileChooserBoxUtil.this.nmEntidade.equals(DocumentoAnexo.NM_ENTIDADE_NOVOCLIENTE) && SessionLavenderePda.isUsuarioSupervisor()) {
				NovoCliente novoCliente = (NovoCliente) domain;
				documentoAnexoFilter.cdRepresentante = novoCliente.cdRepresentante;
			} else {
				documentoAnexoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
			}
			documentoAnexoFilter.nmEntidade = LavendereFileChooserBoxUtil.this.nmEntidade;
			documentoAnexoFilter.dsChave = LavendereFileChooserBoxUtil.this.vlChave;
			return documentoAnexoFilter;
		}

		private void deleteCheckedDocs() throws SQLException {
	    	int[] checkedItens = listContainer.getCheckedItens();
	    	if (checkedItens.length == 0) {
	    		UiUtil.showErrorMessage(Messages.BOTAO_DELETAR_ANEXO_NENHUM_REGISTRO);
	    		return;
	    	}
	    	if (UiUtil.showConfirmYesNoMessage(Messages.BOTAO_DELETAR_ANEXO_CONFIRM)) {
	    		int checkedItensSize = checkedItens.length;
	    		if((LavenderePdaConfig.isObrigaAnexoDocClienteLimiteCredExtrapolado() || LavenderePdaConfig.isObrigaAnexoDocCondicaoPagamento())
	    				&& domain instanceof Pedido) {
	    			if (!((Pedido)domain).isPedidoAberto() && checkedItensSize >= listContainer.size() ) {
	    				UiUtil.showErrorMessage(Messages.MSG_NAO_E_POSSIVEL_DELETAR_TODOS_COMPROVANTES);
	    	    		return;
	    			}
	    		}
	    			
	    		for (int i = 0; i < checkedItensSize; i++) {
	    			if (insertFile) {
	    				DocumentoAnexoService.getInstance().deleteByRowKey(listContainer.getId(checkedItens[i]));
	    			} else {
	    				DocumentoAnexo documentoAnexoFilter = new DocumentoAnexo();
	    				String[] chave = listContainer.getId(checkedItens[i]).split(";");
	    				documentoAnexoFilter.cdEmpresa = chave[0];
	    				documentoAnexoFilter.cdRepresentante = chave[1];
	    				documentoAnexoFilter.nmEntidade = chave[2];
	    				documentoAnexoFilter.dsChave = chave[3] + ";" + chave[4] + ";" + chave[5] + ";" + chave[6] + ";"  + chave[7];
	    				documentoAnexoFilter.cdDocumentoAnexo = ValueUtil.getIntegerValue(chave[8]);
	    				docAnexoList.removeElement(documentoAnexoFilter);
	    			}
	    		}
	    		list();
	    	}
	    }
		
		@Override
		protected void onFormStart() {
			UiUtil.add(this, listContainer, LEFT, TOP, FILL, FILL);
			addButtonPopup(btNovo);
			addButtonPopup(btDelete);
			addButtonPopup(btFechar);
		}

		@Override
		protected void onFormEvent(Event event) throws SQLException {
			switch (event.type) {
				case ControlEvent.PRESSED: {
					if (event.target == btDelete) {
						deleteCheckedDocs();
					} else if (event.target == btNovo) {
						btNovoClick();
					}
					break;
				}
				case PenEvent.PEN_UP: {
					if(listContainer != null && (event.target instanceof BaseListContainer.Item) && listContainer.isEventoClickUnicoDisparado()) {
						String fileName = ((BaseListContainer.Item)listContainer.getSelectedItem()).items[0];
						if (isImageFormat(fileName)) {
							ImageSliderDocumentoAnexoWindow imageDocumentoAnexoWindow = new ImageSliderDocumentoAnexoWindow(getDomainList(getDomainFilter()));
							imageDocumentoAnexoWindow.setSelectedImage(listContainer.getSelectedIndex());
							imageDocumentoAnexoWindow.reposition();
							imageDocumentoAnexoWindow.popup();
						}
					}
					break;
				}
			}
		}
		
		private void btNovoClick() throws SQLException {
			int resultMessage = UiUtil.showMessage(Messages.BOTAO_NOVO_ANEXO_PEDIDO, Messages.ANEXO_NOVO_ANEXO_ESCOLHA, new String[] { Messages.BOTAO_ABRIR_CAMERA, Messages.BOTAO_ABRIR_DIRETORIO_ARQUIVOS });
			if (resultMessage == 0) {
				ImageSliderDocumentoAnexoWindow imageDocumentoAnexoWindow = new ImageSliderDocumentoAnexoWindow(getDomainList(getDomainFilter()));
				imageDocumentoAnexoWindow.novaFotoFromCamera(domain, nmEntidade);
				list();
			} else {
				fecharWindow();
				LavendereFileChooserBoxUtil fileChooserBoxUtil = new LavendereFileChooserBoxUtil(GroupTypeFile.ALL, nmEntidade, domain, setDadosAlteracao, insertFile, docAnexoList);
				fileChooserBoxUtil.popup();
			}
		}
		
		private boolean isImageFormat(String fileName) {
			return TypeFileUtil.isImageTcFormat(fileName);
		}
		
		@Override
		protected void btFecharClick() throws SQLException {
			super.btFecharClick();
			docAnexoList = null;
		}
	}
	
}
