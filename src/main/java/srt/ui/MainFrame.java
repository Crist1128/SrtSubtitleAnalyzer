package srt.ui;

import srt.core.SrtMain;
import srt.core.SrtNode;
import srt.listener.OnLoadSrtFileListener;
import srt.listener.OnOperateSrtNodesListener;
import srt.listener.OnSaveSrtFileListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;

public class MainFrame extends JFrame {

    // �������������ĳ���
    private static final int SEARCH_OPERATION_1 = 0; // ����SID����
    private static final int SEARCH_OPERATION_2 = 1; // ����ʱ������ڵ���Ļ

    private JTextArea textArea;
    private SrtMain srtMain;

    public MainFrame() {
        srtMain = new SrtMain();
        srtMain.setOnLoadSrtFileListener(new SrtFileLoadListener());
        setTitle("SRT��Ļ������");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initializeComponents();
    }

    private void initializeComponents() {
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        addButton(buttonPanel, "����SRT�ļ�", e -> loadSrtFile());
        addButton(buttonPanel, "�����޸�", e -> saveSrtFile());
        addButton(buttonPanel, "ʱ���ƶ�", e -> shiftTime());
        addButton(buttonPanel, "������Ļ", e -> searchSubtitle());
        addButton(buttonPanel, "��ӡ��Ļ", e -> printSrtContent());
        addButton(buttonPanel, "�˳�", e -> System.exit(0));

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addButton(JPanel panel, String label, ActionListener actionListener) {
        JButton button = new JButton(label);
        button.addActionListener(actionListener);
        panel.add(button);
    }

    private void loadSrtFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            srtMain.loadSrtFile(selectedFile.getAbsolutePath());
        }
    }

    private void saveSrtFile() {
        // ����һ���ļ�ѡ����
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("�����ļ�");

        // �����ļ�ѡ������Ĭ����ΪΪ�����ļ�
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);

        // �Ƽ����ļ���
        fileChooser.setSelectedFile(new File("MySubtitles.srt"));

        // ��ʾ�����ļ��ĶԻ���
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // ʹ��srtMain��saveSrtFile���������ļ����û�ѡ���λ��
            srtMain.saveSrtFile(fileToSave.getAbsolutePath(), new OnSaveSrtFileListener() {
                @Override
                public void onSaveSrtFileSuccess() {
                    JOptionPane.showMessageDialog(null, "�ļ��ѱ��浽: " + fileToSave.getAbsolutePath());
                }

                @Override
                public void onSaveSrtFileFail(Exception e) {
                    JOptionPane.showMessageDialog(null, "����ʧ��: " + e.getMessage());
                }
            });
        }
    }


    private void shiftTime() {
        // ���������ֶκ������˵�
        JComboBox<String> shiftTypeComboBox = new JComboBox<>(new String[]{"ǰ�� (-)", "���� (+)"});
        JTextField timeField = new JTextField(5);

        // ������岢������
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("ѡ��ʱ���ƶ�����:"));
        myPanel.add(shiftTypeComboBox);
        myPanel.add(Box.createHorizontalStrut(15)); // ���
        myPanel.add(new JLabel("ʱ�䣨���룩:"));
        myPanel.add(timeField);

        // ��ʾ�Ի���
        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "������ʱ���ƶ�����", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String shiftType = shiftTypeComboBox.getSelectedIndex() == 0 ? "-" : "+";
            String timeStr = timeField.getText();
            try {
                int time = Integer.parseInt(timeStr);
                HashMap<String, Object> param = new HashMap<>();
                param.put("shiftType", shiftType);
                param.put("srtMsecond", time);

                // ʹ��srtMain��operateSrtNodes����ִ��ʱ���ƶ�
                srtMain.operateSrtNodes("SrtTimeShift", param, new OnOperateSrtNodesListener() {
                    @Override
                    public void onOperationStart() {
                        // �ڲ�����ʼʱ����UI�������Ҫ��
                    }

                    @Override
                    public void onOperationSuccess(SrtNode node) {
                        // �������ڵ�����ɹ�������������Ҫ��
                    }

                    @Override
                    public void onOperationSuccess(List<SrtNode> srtNodeList) {
                        JOptionPane.showMessageDialog(null, "�ɹ��ƶ�������Ļ��ʱ���ᣡ");
                        // ˢ����ʾ����Ļ
                        refreshDisplayedSubtitles();
                    }

                    @Override
                    public void onOperationFail(Exception e) {
                        JOptionPane.showMessageDialog(null, "�ƶ���Ļʱ����ʧ�ܣ�ԭ��" + e.toString());
                    }
                });

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "��������ȷ��ʱ��ֵ��");
            }
        }
    }

    // �·�����ˢ����ʾ����Ļ
    private void refreshDisplayedSubtitles() {
        StringBuilder content = new StringBuilder();
        List<SrtNode> nodes = srtMain.getSrtNodeList();
        for (SrtNode node : nodes) {
            content.append(node.toString()).append("\n\n");  // ȷ��SrtNode�����ʵ���toString()����
        }
        textArea.setText(content.toString());
    }

    private void searchSubtitle() {
        // �������������Ի���
        String[] options = {"����SID����", "����ʱ������ڵ���Ļ"};
        int searchOp = JOptionPane.showOptionDialog(this,
                "ѡ����ҷ�ʽ��",
                "������Ļ",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        HashMap<String, Object> searchParam = new HashMap<>();
        try {
            if (searchOp == SEARCH_OPERATION_1) {
                String sidStr = JOptionPane.showInputDialog("������ҵ�Sid��");
                int sid = Integer.parseInt(sidStr);
                searchParam.put("sid", sid);
            } else if (searchOp == SEARCH_OPERATION_2) {
                String timeStr = JOptionPane.showInputDialog("����ʱ��㣺����ʽ��Сʱ ���� �� ���룩");
                String[] timeParts = timeStr.split(" ");
                int[] time = new int[4];
                for (int i = 0; i < 4; i++) {
                    time[i] = Integer.parseInt(timeParts[i]);
                }
                searchParam.put("hour", time[0]);
                searchParam.put("minute", time[1]);
                searchParam.put("second", time[2]);
                searchParam.put("msecond", time[3]);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "�������������ԣ�");
            return;
        }

        srtMain.operateSrtNodes("SrtNodeSearch", searchParam, new OnOperateSrtNodesListener() {
            @Override
            public void onOperationStart() {
                // ���������ﴦ��ʼ����ʱ��UI����
            }

            @Override
            public void onOperationSuccess(SrtNode node) {
                // ���������ɹ����߼�������textArea����ʾ�ҵ�����Ļ
                textArea.setText("���ҳɹ���������Ļ��Ϣ���£�\n" + node.toString());
            }

            @Override
            public void onOperationSuccess(List<SrtNode> srtNodes) {
                // ����������ض����������������ﴦ��
            }

            @Override
            public void onOperationFail(Exception e) {
                JOptionPane.showMessageDialog(null, "����ʧ�ܣ�ԭ��" + e.toString());
            }
        });
    }


    private void printSrtContent() {
        List<SrtNode> nodes = srtMain.getSrtNodeList(); // ��SrtMain��ȡ��Ļ�ڵ��б�
        StringBuilder content = new StringBuilder();
        for (SrtNode node : nodes) {
            content.append(node.toString()).append("\n\n"); // ����SrtNode.toString()�Ѿ�ʵ��
        }
        textArea.setText(content.toString()); // ����Ļ�������õ��ı�����
    }


    class SrtFileLoadListener implements OnLoadSrtFileListener {
        @Override
        public void onLoadSrtFileStart() {
            JOptionPane.showMessageDialog(null, "��ʼ�����ļ�...");
        }

        @Override
        public void onLoadSrtFileSuccess(List<SrtNode> list) {
            JOptionPane.showMessageDialog(null, "�ļ����سɹ���");
            printSrtContent();  // ��ʾ���ص���Ļ����
        }

        @Override
        public void onLoadSrtFileFail(Exception e) {
            JOptionPane.showMessageDialog(null, "����ʧ��: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
