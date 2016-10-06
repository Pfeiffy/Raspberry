package simplepopupmenu.popup.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import simplepopupmenu.Activator;

public class SimpleMessageAction implements IObjectActionDelegate {
	private Shell shell;

	public SimpleMessageAction() {
		super();
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
		shell.setText("kxcjvlbgjlxckjgklsdfgjdfkl");
	}

	@Override
	public void run(IAction action) {
		try {
			// get editor
			IEditorPart editorPart = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();

			if (editorPart instanceof AbstractTextEditor) {
				int offset = 0;
				int length = 0;
				String selectedText = null;
				IEditorSite iEditorSite = editorPart.getEditorSite();
				if (iEditorSite != null) {
					// get selection provider
					ISelectionProvider selectionProvider = iEditorSite.getSelectionProvider();
					if (selectionProvider != null) {
						ISelection iSelection = selectionProvider.getSelection();
						// offset
						offset = ((ITextSelection) iSelection).getOffset();
						if (!iSelection.isEmpty()) {
							selectedText = ((ITextSelection) iSelection).getText();

							// length
							length = ((ITextSelection) iSelection).getLength();
							// System.out.println("length: " + length);
							// MessageDialog.openInformation(shell, "Do
							// Something Menu",
							// "Length: " + length + " Offset: " + offset + " "
							// + selectedText);

							String ZwA = "System.out.println(\" " + selectedText + ": \"+" + selectedText + ");";

							System.out.println(" testversuch: testversuch");

							Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(ZwA),
									null);
							
							

						}
					}
				}

			}
		} catch (Exception e) {
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

}