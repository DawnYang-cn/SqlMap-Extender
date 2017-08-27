package burp;

/**
 * Created by dawnyang on 2017/8/27.
 */

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class BurpExtender implements IBurpExtender,ITab{

    public PrintWriter stdout;
    public IExtensionHelpers hps;
    public IBurpExtenderCallbacks cbs;

    public JPanel jPanelMain;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)         {
        //设置插件名称
        callbacks.setExtensionName("BurpExtender");

        this.hps = callbacks.getHelpers();
        this.cbs = callbacks;
        this.stdout = new PrintWriter(callbacks.getStdout(), true);

        this.stdout.println("hello burp!");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                jPanelMain = new JPanel();

                JButton jButton = new JButton("打开SQL-MAP");

                jButton.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e){
                        stdout.println("打开SQL-MAP");
                        String line = null;
                        StringBuffer stringBuffer = new StringBuffer();
                        Runtime runtime = Runtime.getRuntime();
                        try{
                            Process process = runtime.exec("ifconfig");
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            while((line = bufferedReader.readLine())!=null){
                                stringBuffer.append(line+"\n");
                                stdout.println(stringBuffer);
                            }

                        }
                        catch (IOException e1){
                            e1.printStackTrace();
                        }
                    }

                });


                // 将按钮添加到 主面板 jPanelMain 中.
                jPanelMain.add(jButton);

                // 设置自定义组件并添加标签
                cbs.customizeUiComponent(jPanelMain);
                cbs.addSuiteTab(BurpExtender.this);
            }
        });
    }

    // 实现 ITab 接口的 getTabCaption 方法
    @Override
    public String getTabCaption() {
        return "SQL-MAP";
    }

    // 实现 ITab 接口的 getUiComponent 方法
    @Override
    public Component getUiComponent() {
        return jPanelMain;
    }
}