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

    // 定义搜索操作的常量
    private static final int SEARCH_OPERATION_1 = 0; // 根据SID查找
    private static final int SEARCH_OPERATION_2 = 1; // 查找时间点所在的字幕

    private JTextArea textArea;
    private SrtMain srtMain;

    public MainFrame() {
        srtMain = new SrtMain();
        srtMain.setOnLoadSrtFileListener(new SrtFileLoadListener());
        setTitle("SRT字幕分析器");
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

        addButton(buttonPanel, "加载SRT文件", e -> loadSrtFile());
        addButton(buttonPanel, "保存修改", e -> saveSrtFile());
        addButton(buttonPanel, "时间移动", e -> shiftTime());
        addButton(buttonPanel, "搜索字幕", e -> searchSubtitle());
        addButton(buttonPanel, "打印字幕", e -> printSrtContent());
        addButton(buttonPanel, "退出", e -> System.exit(0));

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
        // 创建一个文件选择器
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("保存文件");

        // 设置文件选择器的默认行为为保存文件
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);

        // 推荐的文件名
        fileChooser.setSelectedFile(new File("MySubtitles.srt"));

        // 显示保存文件的对话框
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // 使用srtMain的saveSrtFile方法保存文件到用户选择的位置
            srtMain.saveSrtFile(fileToSave.getAbsolutePath(), new OnSaveSrtFileListener() {
                @Override
                public void onSaveSrtFileSuccess() {
                    JOptionPane.showMessageDialog(null, "文件已保存到: " + fileToSave.getAbsolutePath());
                }

                @Override
                public void onSaveSrtFileFail(Exception e) {
                    JOptionPane.showMessageDialog(null, "保存失败: " + e.getMessage());
                }
            });
        }
    }


    private void shiftTime() {
        // 创建输入字段和下拉菜单
        JComboBox<String> shiftTypeComboBox = new JComboBox<>(new String[]{"前移 (-)", "后移 (+)"});
        JTextField timeField = new JTextField(5);

        // 创建面板并添加组件
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("选择时间移动类型:"));
        myPanel.add(shiftTypeComboBox);
        myPanel.add(Box.createHorizontalStrut(15)); // 间隔
        myPanel.add(new JLabel("时间（毫秒）:"));
        myPanel.add(timeField);

        // 显示对话框
        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "请输入时间移动参数", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String shiftType = shiftTypeComboBox.getSelectedIndex() == 0 ? "-" : "+";
            String timeStr = timeField.getText();
            try {
                int time = Integer.parseInt(timeStr);
                HashMap<String, Object> param = new HashMap<>();
                param.put("shiftType", shiftType);
                param.put("srtMsecond", time);

                // 使用srtMain的operateSrtNodes方法执行时间移动
                srtMain.operateSrtNodes("SrtTimeShift", param, new OnOperateSrtNodesListener() {
                    @Override
                    public void onOperationStart() {
                        // 在操作开始时更新UI（如果需要）
                    }

                    @Override
                    public void onOperationSuccess(SrtNode node) {
                        // 处理单个节点操作成功的情况（如果需要）
                    }

                    @Override
                    public void onOperationSuccess(List<SrtNode> srtNodeList) {
                        JOptionPane.showMessageDialog(null, "成功移动所有字幕的时间轴！");
                        // 刷新显示的字幕
                        refreshDisplayedSubtitles();
                    }

                    @Override
                    public void onOperationFail(Exception e) {
                        JOptionPane.showMessageDialog(null, "移动字幕时间轴失败！原因：" + e.toString());
                    }
                });

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "请输入正确的时间值！");
            }
        }
    }

    // 新方法：刷新显示的字幕
    private void refreshDisplayedSubtitles() {
        StringBuilder content = new StringBuilder();
        List<SrtNode> nodes = srtMain.getSrtNodeList();
        for (SrtNode node : nodes) {
            content.append(node.toString()).append("\n\n");  // 确保SrtNode类有适当的toString()方法
        }
        textArea.setText(content.toString());
    }

    private void searchSubtitle() {
        // 创建搜索参数对话框
        String[] options = {"根据SID查找", "查找时间点所在的字幕"};
        int searchOp = JOptionPane.showOptionDialog(this,
                "选择查找方式：",
                "搜索字幕",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        HashMap<String, Object> searchParam = new HashMap<>();
        try {
            if (searchOp == SEARCH_OPERATION_1) {
                String sidStr = JOptionPane.showInputDialog("输入查找的Sid：");
                int sid = Integer.parseInt(sidStr);
                searchParam.put("sid", sid);
            } else if (searchOp == SEARCH_OPERATION_2) {
                String timeStr = JOptionPane.showInputDialog("输入时间点：（格式：小时 分钟 秒 毫秒）");
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
            JOptionPane.showMessageDialog(this, "输入有误，请重试！");
            return;
        }

        srtMain.operateSrtNodes("SrtNodeSearch", searchParam, new OnOperateSrtNodesListener() {
            @Override
            public void onOperationStart() {
                // 可以在这里处理开始搜索时的UI反馈
            }

            @Override
            public void onOperationSuccess(SrtNode node) {
                // 处理搜索成功的逻辑，如在textArea中显示找到的字幕
                textArea.setText("查找成功！该条字幕信息如下：\n" + node.toString());
            }

            @Override
            public void onOperationSuccess(List<SrtNode> srtNodes) {
                // 如果搜索返回多个结果，可以在这里处理
            }

            @Override
            public void onOperationFail(Exception e) {
                JOptionPane.showMessageDialog(null, "查找失败！原因：" + e.toString());
            }
        });
    }


    private void printSrtContent() {
        List<SrtNode> nodes = srtMain.getSrtNodeList(); // 从SrtMain获取字幕节点列表
        StringBuilder content = new StringBuilder();
        for (SrtNode node : nodes) {
            content.append(node.toString()).append("\n\n"); // 假设SrtNode.toString()已经实现
        }
        textArea.setText(content.toString()); // 将字幕内容设置到文本区域
    }


    class SrtFileLoadListener implements OnLoadSrtFileListener {
        @Override
        public void onLoadSrtFileStart() {
            JOptionPane.showMessageDialog(null, "开始加载文件...");
        }

        @Override
        public void onLoadSrtFileSuccess(List<SrtNode> list) {
            JOptionPane.showMessageDialog(null, "文件加载成功！");
            printSrtContent();  // 显示加载的字幕内容
        }

        @Override
        public void onLoadSrtFileFail(Exception e) {
            JOptionPane.showMessageDialog(null, "加载失败: " + e.getMessage());
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
