package gameClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.UnknownHostException;

public class login_page extends JFrame implements ActionListener {

    int h ;
    int w ;
    JTextField tf_id;
    JTextField tf_level;
    JLabel jl;
    JLabel jl2;
    JLabel err;
    JButton jb;
    Ex2 ex2;

    public login_page() {
        setSize(300,250);
        this.h = getHeight();
        this.w = getWidth();
        setResizable(false);
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tf_id = new JTextField();
        tf_id.setBounds((int)(w*0.5-75), 50, 150, 20);
        tf_level = new JTextField();
        tf_level.setBounds((int)(w*0.5-75), 110, 150, 20);
        jl = new JLabel();
        jl.setBounds((int)(w*0.5-62), 80, 250, 20);
        jl.setText("please choose level:");
        jl2 = new JLabel();
        jl2.setBounds((int)(w*0.5-62), 20, 250, 20);
        jl2.setText("please enter your id:");
        jb = new JButton("Start Game");
        jb.setBounds((int)(w*0.5-75), 150, 150, 30);
        jb.addActionListener(this);
        err = new JLabel();
        err.setBounds((int)(w*0.5-115), 190, 250, 20);
        err.setText("incorrect id or level please try aging...");
        this.add(tf_id);
        this.add(tf_level);
        this.add(jl);
        this.add(jl2);
        this.add(jb);
        setLayout(null);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String id = tf_id.getText();
        String level = tf_level.getText();
       try {
         //  if(Integer.parseInt(id) <100000000||Integer.parseInt(id)>999999999) throw new IllegalArgumentException();
           int lv = Integer.parseInt(level);
           int idnum = Integer.parseInt(id);
           Ex2.iupdate(lv,idnum);
           setVisible(false);

       }
        catch (Exception ex){
           ex.printStackTrace();
            this.add(err);
            repaint();

        }
    }

    public void register(Ex2 BL){
        this.ex2 = BL;
    }
}
