package BrazoRobotico;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



public class CurvedSlider extends JPanel implements MouseListener, MouseMotionListener
{
    private double minAngleRad;
    private double maxAngleRad;
    private double maxAngleRadNew;
    private double minValue;
    private double maxValue;
    private double value;
       
    private double minAngleRadPRNT = 0;
    private double maxAngleRadPRNT = 0;
    private double minValuePRNT = 0;
    private double maxValuePRNT = 0;
    private double valuePRNT = 0;
       
    private int panelServo;
    private int valueSlider;
    CurvedSlider(int panelServo, double maxAngleRad, int value)
    {
        this.minAngleRad = 50 / 100.0 * Math.PI * 2;
        this.maxAngleRad = maxAngleRad / 100.0 * Math.PI * 2;
        this.maxAngleRadNew = 0 / 100.0 * Math.PI * 2;
        this.minValue = 0 / 100.0;
        this.maxValue = 180 / 100.0;
        this.value = value / 100.0;
        this.panelServo = panelServo;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    void setAngles(double minAngleRad, double maxAngleRad)
    {
        this.minAngleRad = minAngleRad;
        this.maxAngleRad = maxAngleRad;
        repaint();
    }

    void setRange(double minValue, double maxValue)
    {
        this.minValue = minValue;
        this.maxValue = maxValue;
        repaint();
    }

    void setValue(double value)
    {
        this.value = value;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics gr)
    {
        super.paintComponent(gr);
        Graphics2D g = (Graphics2D)gr;
        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING, 
            RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0,0,getWidth(),getHeight());

        boolean printValues = false;
        printValues = true;
        if (printValues)
        {
            int ty = 20;
            minAngleRadPRNT = Math.toDegrees(minAngleRad);
            maxAngleRadPRNT = Math.toDegrees(maxAngleRad);
            minValuePRNT = minValue;
            maxValuePRNT = maxValue;
            valuePRNT = value;
            g.setColor(Color.BLACK);
            /*g.drawString("minAngle "+minAngleRadPRNT, 20, ty+=20);
            g.drawString("maxAngle "+maxAngleRadPRNT, 20, ty+=20);
            g.drawString("minValue "+minValuePRNT, 20, ty+=20);
            g.drawString("maxValue "+maxValuePRNT, 20, ty+=20);*/
            g.setFont(new Font("Cenury gothic", 4, 35));
            if (this.panelServo == main.PINZA) {
                this.valueSlider = ((int) Math.round(maxAngleRadPRNT) / 4) + 136;
            } else {
                this.valueSlider = (int) Math.round(maxAngleRadPRNT);
            }
            
            int x = ((this.valueSlider < 10) ? 110 : ((this.valueSlider < 100) ? 123: 140) );
            g.drawString(180 - (this.valueSlider) + "°", x, 165);
        }

        double alpha = (value - minValue) / (maxValue - minValue);
        double angleRad = minAngleRad + alpha * (maxAngleRad - minAngleRad);

        double radius = Math.min(getWidth(), getHeight()) / 3.0;

        final double thickness = 30;
        double xC = getWidth() / 2.0;
        double yC = getHeight() / 2.0;
        double x0 = xC + Math.cos(angleRad) * (radius - thickness);
        double y0 = yC - Math.sin(angleRad) * (radius - thickness);
        double x1 = xC + Math.cos(angleRad) * radius;
        double y1 = yC - Math.sin(angleRad) * radius;

        
        Shape background01 = new Arc2D.Double(
            xC-radius, yC-radius, 
            radius+radius, radius+radius, 
            Math.toDegrees(maxAngleRadNew), 
            Math.toDegrees(maxAngleRadNew-minAngleRad), 
            Arc2D.PIE);

        Shape background11 = new Ellipse2D.Double(
            xC-radius+thickness, yC-radius+thickness, 
            radius+radius-thickness-thickness, 
            radius+radius-thickness-thickness);

        Area b = new Area(background01);
        b.subtract(new Area(background11));

        g.setColor(Color.BLACK);
        g.fill(b);
        g.setStroke(new BasicStroke(3.0f, 
            BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(Color.LIGHT_GRAY);
        g.draw(b);

        g.setStroke(new BasicStroke(15.0f, 
            BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(Color.DARK_GRAY);
        g.draw(new Line2D.Double(x0, y0, x1, y1));
        
        
        
        
        Shape background0 = new Arc2D.Double(
            xC-radius, yC-radius, 
            radius+radius, radius+radius, 
            Math.toDegrees(minAngleRad), 
            Math.toDegrees(maxAngleRad-minAngleRad), 
            Arc2D.PIE);

        Shape background1 = new Ellipse2D.Double(
            xC-radius+thickness, yC-radius+thickness, 
            radius+radius-thickness-thickness, 
            radius+radius-thickness-thickness);

        Area a = new Area(background0);
        a.subtract(new Area(background1));

        g.setColor(Color.RED);
        g.fill(a);
        g.setStroke(new BasicStroke(3.0f, 
            BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(Color.LIGHT_GRAY);
        g.draw(a);

        g.setStroke(new BasicStroke(15.0f, 
            BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(Color.DARK_GRAY);
        g.draw(new Line2D.Double(x0, y0, x1, y1));
    }

    private void updateAngle(Point p)
    {
        
        double xC = getWidth() / 2.0;
        double yC = getHeight() / 2.0;
        double dx = p.getX() - xC;
        double dy = p.getY() - yC;
        double angleRad = Math.atan2(-dy, dx);
        if (angleRad < -Math.PI / 2)
        {
            angleRad = 2 * Math.PI + angleRad;
        }
        angleRad = Math.max(maxAngleRad, Math.min(minAngleRad, angleRad));
        double alpha = (angleRad - minAngleRad) / (maxAngleRad - minAngleRad);
        double value = (minValue + alpha * (maxValue - minValue));
        
        double max = (((angleRad) - (minAngleRad/100)));
        if (max < 0) {
            return;
        }
        setAngles(minAngleRad, max);
        setValue(value);
        System.out.println("Panel Padree::=> " + this.panelServo);
        int variableSum = 0;
        switch(this.panelServo) {
            case main.BASE: 
                variableSum = 0;
                break;
            case main.BRAZODERECHO: 
                variableSum = 63;
                break;
            case main.BRAZOIZQUIERDO: 
                variableSum = 126;
                break;
            case main.PINZA: 
                variableSum = 189;
                break;
        }
        System.out.println("Grados: " + (180 - this.valueSlider));
        System.out.println("Serial: " + (Map.rTres(180 - this.valueSlider) + variableSum));

        main.conexion.enviar(Map.rTres(180 - this.valueSlider) + variableSum);
        /*System.out.println("----------------------------------------------");
        System.out.println("angleRad: " + angleRad);
        System.out.println("minAngleRad: " + minAngleRad);
        System.out.println("MaAngle=>>> " + maxAngleRadPRNT);*/
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        updateAngle(e.getPoint());
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        //updateAngle(e.getPoint());
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
    }
}
