package Perceptron;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Visualizer extends JPanel {
    private List<Observation> testData;
    private double w0, w1, threshold;

    public Visualizer(List<Observation> testData, double[] weights, double threshold) {
        this.testData = testData;
        this.w0 = weights[0];
        this.w1 = weights[1];
        this.threshold = threshold;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        double minX = Double.MAX_VALUE, maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE, maxY = -Double.MAX_VALUE;

        for (Observation obs : testData) {
            double[] features = obs.getFeatures();
            if (features[0] < minX)
                minX = features[0];
            if (features[0] > maxX)
                maxX = features[0];
            if (features[1] < minY)
                minY = features[1];
            if (features[1] > maxY)
                maxY = features[1];
        }

        if (minX > 0)
            minX = 0;
        if (maxX < 0)
            maxX = 0;
        if (minY > 0)
            minY = 0;
        if (maxY < 0)
            maxY = 0;

        double paddingX = (maxX - minX) == 0 ? 1 : (maxX - minX) * 0.2;
        double paddingY = (maxY - minY) == 0 ? 1 : (maxY - minY) * 0.2;

        minX -= paddingX;
        maxX += paddingX;
        minY -= paddingY;
        maxY += paddingY;

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(1));

        int originX = (int) ((0 - minX) / (maxX - minX) * width);
        int originY = height - (int) ((0 - minY) / (maxY - minY) * height);

        if (originX >= 0 && originX <= width) {
            g2d.drawLine(originX, 0, originX, height);
        }
        if (originY >= 0 && originY <= height) {
            g2d.drawLine(0, originY, width, originY);
        }

        for (Observation obs : testData) {
            double[] features = obs.getFeatures();
            double x = features[0];
            double y = features[1];

            int screenX = (int) ((x - minX) / (maxX - minX) * width);
            int screenY = height - (int) ((y - minY) / (maxY - minY) * height);

            if (obs.getLabel() == 1.0) {
                g2d.setColor(Color.RED);
                g2d.fillOval(screenX - 5, screenY - 5, 10, 10);
            } else {
                g2d.setColor(Color.BLUE);
                g2d.fillRect(screenX - 5, screenY - 5, 10, 10);
            }
        }

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));

        if (w1 != 0) {
            double x1 = minX;
            double y1 = (threshold - w0 * x1) / w1;
            double x2 = maxX;
            double y2 = (threshold - w0 * x2) / w1;

            int screenX1 = (int) ((x1 - minX) / (maxX - minX) * width);
            int screenY1 = height - (int) ((y1 - minY) / (maxY - minY) * height);
            int screenX2 = (int) ((x2 - minX) / (maxX - minX) * width);
            int screenY2 = height - (int) ((y2 - minY) / (maxY - minY) * height);

            g2d.drawLine(screenX1, screenY1, screenX2, screenY2);
        } else if (w0 != 0) {
            double xLine = threshold / w0;
            int screenX = (int) ((xLine - minX) / (maxX - minX) * width);
            g2d.drawLine(screenX, 0, screenX, height);
        }

        int padding = 10;
        int legendWidth = 140;
        int legendHeight = 60;
        int startX = width - legendWidth - padding;
        int startY = padding;

        g2d.setColor(Color.WHITE);
        g2d.fillRect(startX, startY, legendWidth, legendHeight);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(startX, startY, legendWidth, legendHeight);

        g2d.setColor(Color.RED);
        g2d.fillOval(startX + 10, startY + 15, 10, 10);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Iris setosa (1.0)", startX + 30, startY + 25);

        g2d.setColor(Color.BLUE);
        g2d.fillRect(startX + 10, startY + 35, 10, 10);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Iris versicolor (0.0)", startX + 30, startY + 45);
    }

    public static void showPlot(List<Observation> testData, double[] weights, double threshold) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Perceptron Decision Boundary (Test Data)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new Visualizer(testData, weights, threshold));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
