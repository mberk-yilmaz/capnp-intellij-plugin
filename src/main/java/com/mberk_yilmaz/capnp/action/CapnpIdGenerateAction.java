package com.mberk_yilmaz.capnp.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.mberk_yilmaz.capnp.psi.CapnpFile;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;

/**
 * Action to generate and insert a unique Cap'n Proto file ID.
 * 
 * A Cap'n Proto file must have a unique 64-bit ID, typically written as @0xHEXVALUE;
 * This action generates a random 64-bit ID and inserts it at the beginning of the file.
 */
public class CapnpIdGenerateAction extends AnAction {

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        // Use BGT (Background Thread) since we're accessing PSI data in update()
        return ActionUpdateThread.BGT;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (psiFile == null || !(psiFile instanceof CapnpFile)) {
            Messages.showErrorDialog("This action only works on Cap'n Proto files (.capnp)",
                    "Invalid File");
            return;
        }

        CapnpFile capnpFile = (CapnpFile) psiFile;
        Document document = PsiDocumentManager.getInstance(psiFile.getProject())
                .getDocument(psiFile);
        
        if (document == null) {
            Messages.showErrorDialog("Unable to access document",
                    "Error");
            return;
        }
        
        // Check if file ID already exists
        String fileText = document.getText();
        if (fileText.trim().startsWith("@0x")) {
            int response = Messages.showYesNoDialog(
                    "This file already has an ID. Do you want to replace it?",
                    "File ID Exists",
                    "Replace",
                    "Cancel",
                    Messages.getQuestionIcon());
            if (response != Messages.YES) {
                return;
            }
        }

        // Generate a new random 64-bit ID
        String newId = generateRandomFileId();

        // Insert the ID at the beginning of the file
        WriteCommandAction.runWriteCommandAction(
                psiFile.getProject(),
                () -> {
                    String text = document.getText();
                    
                    // Remove existing file ID if present (for replacement)
                    if (text.trim().startsWith("@0x")) {
                        int endOfLine = text.indexOf('\n');
                        if (endOfLine == -1) {
                            endOfLine = text.length();
                        } else {
                            endOfLine++; // Include the newline
                        }
                        text = text.substring(endOfLine).trim();
                    }
                    
                    // Insert new ID with proper formatting
                    String newText = newId + "\n\n" + text;
                    document.setText(newText);

                    Messages.showInfoMessage(
                            "Generated Cap'n Proto file ID: " + newId,
                            "Success");
                });
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        e.getPresentation().setEnabled(psiFile instanceof CapnpFile);
        if (!(psiFile instanceof CapnpFile)) {
            e.getPresentation().setVisible(false);
        }
    }

    /**
     * Generate a random 64-bit Cap'n Proto file ID.
     * Format: @0x[16 hex characters];
     * Note: The first bit is always 1 (0x8000000000000000 and higher).
     * 
     * @return A random Cap'n Proto file ID string (e.g., "@0xdbb9ad1f14bf0b36;")
     */
    private String generateRandomFileId() {
        SecureRandom random = new SecureRandom();
        
        // Generate a 64-bit random ID
        // Ensure the first bit is 1 (0x8000000000000000 minimum) by setting the high bit
        long id = random.nextLong();
        // Set the highest bit to 1 to ensure it's a valid Cap'n Proto ID
        id = id | 0x8000000000000000L;
        
        // Convert to hex string without sign extension
        String hex = String.format("%016x", id);
        return "@0x" + hex + ";";
    }
}
