/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.base;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jlab.groot.ui.GraphicsAxis;
import org.jlab.groot.ui.LatexText;

/**
 *
 * @author kenjo
 */
public class TColorPalette {

    private double stops[] = {0.0000, 0.1250, 0.2500, 0.3750, 0.5000, 0.6250, 0.7500, 0.8750, 1.0000};
    private Color paletteColors[];
    private PaletteName activePalette;
    private Color bgColor = new Color(200, 200, 200);

    public TColorPalette() {
        setPalette(PaletteName.kDefault);
       //setPalette(PaletteName.kDarkBodyRadiator);
    }

    private TColorPalette(PaletteName palName, Color bgColor) {
        setPalette(palName);
        this.bgColor = bgColor;
    }

    public TColorPalette(TColorPalette palette) {
        this(palette.activePalette, palette.bgColor);
    }

    public Color getColor3D(int icolor) {
        if (icolor < 0 || icolor > paletteColors.length - 1) {
            throw new UnsupportedOperationException("No color id:" + icolor);
        }
        return paletteColors[icolor];
    }

    public Color getColor3D(double value, double max, boolean islog) {
        return getColor3D(value, 0, max, islog);
    }

    public Color getColor3D(double value, double min, double max, boolean islog) {
        double fraction = 0.0;
        if (max < min) {
            throw new UnsupportedOperationException("axis range is wrong: Maximum < Minimum");
        }
        if (value == 0 && min >= 0) {
            return bgColor;
        }
        if (islog == true) {
            if (value <= 0) {
                throw new UnsupportedOperationException("Logarithmic scale can't be enabled for negative values");
            }
            if (min == 0) {
                min = 0.1;
            }
            value = Math.log10(value);
            min = Math.log10(min);
            max = Math.log10(max);
        }
        fraction = (value - min) / (max - min);

        if (fraction > 1) {
            fraction = 1.0;
        } else if (fraction < 0) {
            fraction = 0;
        }
        return paletteColors[(int) (fraction * (paletteColors.length - 1))];
    }

    public void draw(Graphics2D g2d, int x, int y, int width, int height,
            double axismin, double axismax, Boolean logFlag) {
        //System.out.println("plotting color paletter");

        GraphicsAxis zAxis = new GraphicsAxis();
        zAxis.setLog(logFlag);
        zAxis.setDimension(y, y + height);
        zAxis.setRange(axismin, axismax);

        int ncolors = getPaletteSize();
        for (int i = 0; i < ncolors; i++) {
            g2d.setColor(getColor3D(i));
            int yp = (int) (((double) i * height) / ncolors);
            int offset = (int) (((double) (i + 1) * height) / ncolors);
            int length = offset - yp;
            //System.out.println("drawing ");
            g2d.fillRect(x, y + height - yp - length, width, offset - yp + 1);
        }

        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, width, height);

        List<Double> axisTicks = zAxis.getAxisTicks();
        List<LatexText> axisTexts = zAxis.getAxisLabels();

        for (int i = 0; i < axisTicks.size(); i++) {
            int xc = x + width;
            int yc = (int) (zAxis.getDimension().getMin() + zAxis.getDimension().getMax()
                    - zAxis.getAxisPosition(axisTicks.get(i)));
            g2d.drawLine(xc, yc, xc + 4, yc);
            //System.out.println("drawing ticks " + xc + "  " + yc);
            axisTexts.get(i).drawString(g2d, xc + 8, yc, 0, 1);
        }
    }

    public enum PaletteName {
        kDefault(50),
        kDeepSea(51),
        kGreyScale(52),
        kDarkBodyRadiator(53),
        kBlueYellow(54),
        kRainBow(55),
        kInvertedDarkBodyRadiator(56),
        kBird(57),
        kCubehelix(58),
        kGreenRedViolet(59),
        kBlueRedYellow(60),
        kOcean(61),
        kColorPrintableOnGrey(62),
        kAlpine(63),
        kAquamarine(64),
        kArmy(65),
        kAtlantic(66),
        kAurora(67),
        kAvocado(68),
        kBeach(69),
        kBlackBody(70),
        kBlueGreenYellow(71),
        kBrownCyan(72),
        kCMYK(73),
        kCandy(74),
        kCherry(75),
        kCoffee(76),
        kDarkRainBow(77),
        kDarkTerrain(78),
        kFall(79),
        kFruitPunch(80),
        kFuchsia(81),
        kGreyYellow(82),
        kGreenBrownTerrain(83),
        kGreenPink(84),
        kIsland(85),
        kLake(86),
        kLightTemperature(87),
        kLightTerrain(88),
        kMint(89),
        kNeon(90),
        kPastel(91),
        kPearl(92),
        kPigeon(93),
        kPlum(94),
        kRedBlue(95),
        kRose(96),
        kRust(97),
        kSandyTerrain(98),
        kSienna(99),
        kSolar(100),
        kSouthWest(101),
        kStarryNight(102),
        kSunset(103),
        kTemperatureMap(104),
        kThermometer(105),
        kValentine(106),
        kVisibleSpectrum(107),
        kWaterMelon(108),
        kCool(109),
        kCopper(110),
        kGistEarth(111),
        kViridis(112);

        private int value;
        private static final Map<Object, PaletteName> map = new HashMap<>();

        private PaletteName(int value) {
            this.value = value;
        }

        static {
            for (PaletteName palName : PaletteName.values()) {
                map.put(palName.name(), palName);
                map.put(palName.value, palName);
            }
        }

        public static PaletteName valueOf(int palVal) {
            if (!map.containsKey(palVal)) {
                throw new UnsupportedOperationException("Unknown value for palette map: " + palVal);
            }
            return map.get(palVal);
        }

        public static PaletteName nameOf(String palName) {
            if (!map.containsKey(palName)) {
                throw new UnsupportedOperationException("Unknown value for palette map: " + palName);
            }
            return map.get(palName);
        }
    }

    private void CreateGradientColorTable(double[] red, double[] green, double[] blue) {
        if (red.length == 47) {
            paletteColors = new Color[red.length];
            for (int icol = 0; icol < red.length; icol++) {
                paletteColors[icol] = new Color((float) red[icol], (float) green[icol], (float) blue[icol]);
            }
        } else {
            paletteColors = new Color[255];
            int nstops = stops.length;
            int icolor = 0;
            for (int istop = 1; istop < nstops; istop++) {
                int ncolors = (int) (Math.floor(255.0 * stops[istop]) - Math.floor(255.0 * stops[istop - 1]));
                for (int ic = 0; ic < ncolors; ic++) {
                    double rr = red[istop - 1] + ic * (red[istop] - red[istop - 1]) / ncolors;
                    double gg = green[istop - 1] + ic * (green[istop] - green[istop - 1]) / ncolors;
                    double bb = blue[istop - 1] + ic * (blue[istop] - blue[istop - 1]) / ncolors;
                    paletteColors[icolor++] = new Color((int) rr, (int) gg, (int) bb);
                }
            }
        }
    }

    public int getPaletteSize() {
        return paletteColors.length;
    }

    public void setPalette(String palName) {
        setPalette(PaletteName.nameOf(palName));
    }

    public void setPalette(int palVal) {
        setPalette(PaletteName.valueOf(palVal));
    }

    public PaletteName getActivePalette() {
        return activePalette;
    }

    public void setBackgroundColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public final void setPalette(PaletteName palette) {
        activePalette = palette;

        switch (palette) {
            case kDefault: {
                double[] red = {0., 0., 0., 0., 0., 0., 0., 0., 0., 0.,
                    0., 0., 0., 0., 0., 0., 0., 0., 0., 0.,
                    0., 0., 0., 0.17, 0.33, 0.50, 0.67, 0.83, 1.00, 1.00,
                    1., 1., 1., 1., 1., 1., 1., 1., 1., 1.,
                    1., 1., 1., 1., 1., 1., 1.};
                double[] green = {0., 0., 0., 0., 0., 0., 0., 0., 0., 0.,
                    0., 0.08, 0.15, 0.23, 0.31, 0.38, 0.46, 0.53, 0.59, 0.66,
                    0.73, 0.80, 0.87, 0.72, 0.58, 0.43, 0.29, 0.14, 0.00, 0.08,
                    0.17, 0.25, 0.33, 0.42, 0.50, 0.58, 0.67, 0.75, 0.83, 0.92,
                    1., 1., 1., 1., 1., 1., 1.};
                double[] blue = {0.30, 0.33, 0.36, 0.39, 0.42, 0.45, 0.48, 0.52, 0.56, 0.60,
                    0.64, 0.68, 0.68, 0.70, 0.70, 0.70, 0.70, 0.64, 0.56, 0.48,
                    0.40, 0.33, 0., 0., 0., 0., 0., 0., 0., 0.,
                    0., 0., 0., 0., 0., 0., 0., 0., 0., 0.,
                    0., 0.17, 0.33, 0.50, 0.67, 0.83, 1.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Deep Sea
            case kDeepSea: {
                double red[] = {0., 9., 13., 17., 24., 32., 27., 25., 29.};
                double green[] = {0., 0., 0., 2., 37., 74., 113., 160., 221.};
                double blue[] = {28., 42., 59., 78., 98., 129., 154., 184., 221.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Grey Scale
            case kGreyScale: {
                double red[] = {0., 32., 64., 96., 128., 160., 192., 224., 255.};
                double green[] = {0., 32., 64., 96., 128., 160., 192., 224., 255.};
                double blue[] = {0., 32., 64., 96., 128., 160., 192., 224., 255.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Dark Body Radiator
            case kDarkBodyRadiator: {
                double red[] = {0., 45., 99., 156., 212., 230., 237., 234., 242.};
                double green[] = {0., 0., 0., 45., 101., 168., 238., 238., 243.};
                double blue[] = {0., 1., 1., 3., 9., 8., 11., 95., 230.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Two-color hue (dark blue through neutral gray to bright yellow)
            case kBlueYellow: {
                double red[] = {0., 22., 44., 68., 93., 124., 160., 192., 237.};
                double green[] = {0., 16., 41., 67., 93., 125., 162., 194., 241.};
                double blue[] = {97., 100., 99., 99., 93., 68., 44., 26., 74.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Rain Bow
            case kRainBow: {
                double red[] = {0., 5., 15., 35., 102., 196., 208., 199., 110.};
                double green[] = {0., 48., 124., 192., 206., 226., 97., 16., 0.};
                double blue[] = {99., 142., 198., 201., 90., 22., 13., 8., 2.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Inverted Dark Body Radiator
            case kInvertedDarkBodyRadiator: {
                double red[] = {242., 234., 237., 230., 212., 156., 99., 45., 0.};
                double green[] = {243., 238., 238., 168., 101., 45., 0., 0., 0.};
                double blue[] = {230., 95., 11., 8., 9., 3., 1., 1., 0.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Bird
            case kBird: {
                double red[] = {53.0, 15.0, 19.0, 5.0, 45.0, 135.0, 208.0, 253.0, 248.0};
                double green[] = {42.0, 91.0, 128.0, 163.0, 183.0, 191.0, 186.0, 200.0, 250.0};
                double blue[] = {134.0, 221.0, 213.0, 201.0, 163.0, 118.0, 89.0, 50.0, 13.0};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Cubehelix
            case kCubehelix: {
                double red[] = {0.0, 24.0, 2.0, 54.0, 176.0, 235.0, 201.0, 193.0, 255.0};
                double green[] = {0.0, 29.0, 92.0, 128.0, 116.0, 119.0, 176.0, 235.0, 255.0};
                double blue[] = {0.0, 68.0, 79.0, 33.0, 57.0, 171.0, 251.0, 244.0, 255.0};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Green Red Violet
            case kGreenRedViolet: {
                double red[] = {13., 23., 25., 63., 76., 104., 137., 161., 206.};
                double green[] = {95., 67., 37., 21., 0., 12., 35., 52., 79.};
                double blue[] = {4., 3., 2., 6., 11., 22., 49., 98., 208.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Blue Red Yellow
            case kBlueRedYellow: {
                double red[] = {0., 61., 89., 122., 143., 160., 185., 204., 231.};
                double green[] = {0., 0., 0., 0., 14., 37., 72., 132., 235.};
                double blue[] = {0., 140., 224., 144., 4., 5., 6., 9., 13.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Ocean
            case kOcean: {
                double red[] = {14., 7., 2., 0., 5., 11., 55., 131., 229.};
                double green[] = {105., 56., 26., 1., 42., 74., 131., 171., 229.};
                double blue[] = {2., 21., 35., 60., 92., 113., 160., 185., 229.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Color Printable On Grey
            case kColorPrintableOnGrey: {
                double red[] = {0., 0., 0., 70., 148., 231., 235., 237., 244.};
                double green[] = {0., 0., 0., 0., 0., 69., 67., 216., 244.};
                double blue[] = {0., 102., 228., 231., 177., 124., 137., 20., 244.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Alpine
            case kAlpine: {
                double red[] = {50., 56., 63., 68., 93., 121., 165., 192., 241.};
                double green[] = {66., 81., 91., 96., 111., 128., 155., 189., 241.};
                double blue[] = {97., 91., 75., 65., 77., 103., 143., 167., 217.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Aquamarine
            case kAquamarine: {
                double red[] = {145., 166., 167., 156., 131., 114., 101., 112., 132.};
                double green[] = {158., 178., 179., 181., 163., 154., 144., 152., 159.};
                double blue[] = {190., 199., 201., 192., 176., 169., 160., 166., 190.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Army
            case kArmy: {
                double red[] = {93., 91., 99., 108., 130., 125., 132., 155., 174.};
                double green[] = {126., 124., 128., 129., 131., 121., 119., 153., 173.};
                double blue[] = {103., 94., 87., 85., 80., 85., 107., 120., 146.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Atlantic
            case kAtlantic: {
                double red[] = {24., 40., 69., 90., 104., 114., 120., 132., 103.};
                double green[] = {29., 52., 94., 127., 150., 162., 159., 151., 101.};
                double blue[] = {29., 52., 96., 132., 162., 181., 184., 186., 131.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Aurora
            case kAurora: {
                double red[] = {46., 38., 61., 92., 113., 121., 132., 150., 191.};
                double green[] = {46., 36., 40., 69., 110., 135., 131., 92., 34.};
                double blue[] = {46., 80., 74., 70., 81., 105., 165., 211., 225.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Avocado
            case kAvocado: {
                double red[] = {0., 4., 12., 30., 52., 101., 142., 190., 237.};
                double green[] = {0., 40., 86., 121., 140., 172., 187., 213., 240.};
                double blue[] = {0., 9., 14., 18., 21., 23., 27., 35., 101.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Beach
            case kBeach: {
                double red[] = {198., 206., 206., 211., 198., 181., 161., 171., 244.};
                double green[] = {103., 133., 150., 172., 178., 174., 163., 175., 244.};
                double blue[] = {49., 54., 55., 66., 91., 130., 184., 224., 244.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Black Body
            case kBlackBody: {
                double red[] = {243., 243., 240., 240., 241., 239., 186., 151., 129.};
                double green[] = {0., 46., 99., 149., 194., 220., 183., 166., 147.};
                double blue[] = {6., 8., 36., 91., 169., 235., 246., 240., 233.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Blue Green Yellow
            case kBlueGreenYellow: {
                double red[] = {22., 19., 19., 25., 35., 53., 88., 139., 210.};
                double green[] = {0., 32., 69., 108., 135., 159., 183., 198., 215.};
                double blue[] = {77., 96., 110., 116., 110., 100., 90., 78., 70.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Brown Cyan
            case kBrownCyan: {
                double red[] = {68., 116., 165., 182., 189., 180., 145., 111., 71.};
                double green[] = {37., 82., 135., 178., 204., 225., 221., 202., 147.};
                double blue[] = {16., 55., 105., 147., 196., 226., 232., 224., 178.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // CMYK
            case kCMYK: {
                double red[] = {61., 99., 136., 181., 213., 225., 198., 136., 24.};
                double green[] = {149., 140., 96., 83., 132., 178., 190., 135., 22.};
                double blue[] = {214., 203., 168., 135., 110., 100., 111., 113., 22.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Candy
            case kCandy: {
                double red[] = {76., 120., 156., 183., 197., 180., 162., 154., 140.};
                double green[] = {34., 35., 42., 69., 102., 137., 164., 188., 197.};
                double blue[] = {64., 69., 78., 105., 142., 177., 205., 217., 198.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Cherry
            case kCherry: {
                double red[] = {37., 102., 157., 188., 196., 214., 223., 235., 251.};
                double green[] = {37., 29., 25., 37., 67., 91., 132., 185., 251.};
                double blue[] = {37., 32., 33., 45., 66., 98., 137., 187., 251.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Coffee
            case kCoffee: {
                double red[] = {79., 100., 119., 137., 153., 172., 192., 205., 250.};
                double green[] = {63., 79., 93., 103., 115., 135., 167., 196., 250.};
                double blue[] = {51., 59., 66., 61., 62., 70., 110., 160., 250.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Dark Rain Bow
            case kDarkRainBow: {
                double red[] = {43., 44., 50., 66., 125., 172., 178., 155., 157.};
                double green[] = {63., 63., 85., 101., 138., 163., 122., 51., 39.};
                double blue[] = {121., 101., 58., 44., 47., 55., 57., 44., 43.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Dark Terrain
            case kDarkTerrain: {
                double red[] = {0., 41., 62., 79., 90., 87., 99., 140., 228.};
                double green[] = {0., 57., 81., 93., 85., 70., 71., 125., 228.};
                double blue[] = {95., 91., 91., 82., 60., 43., 44., 112., 228.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Fall
            case kFall: {
                double red[] = {49., 59., 72., 88., 114., 141., 176., 205., 222.};
                double green[] = {78., 72., 66., 57., 59., 75., 106., 142., 173.};
                double blue[] = {78., 55., 46., 40., 39., 39., 40., 41., 47.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Fruit Punch
            case kFruitPunch: {
                double red[] = {243., 222., 201., 185., 165., 158., 166., 187., 219.};
                double green[] = {94., 108., 132., 135., 125., 96., 68., 51., 61.};
                double blue[] = {7., 9., 12., 19., 45., 89., 118., 146., 118.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Fuchsia
            case kFuchsia: {
                double red[] = {19., 44., 74., 105., 137., 166., 194., 206., 220.};
                double green[] = {19., 28., 40., 55., 82., 110., 159., 181., 220.};
                double blue[] = {19., 42., 68., 96., 129., 157., 188., 203., 220.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Grey Yellow
            case kGreyYellow: {
                double red[] = {33., 44., 70., 99., 140., 165., 199., 211., 216.};
                double green[] = {38., 50., 76., 105., 140., 165., 191., 189., 167.};
                double blue[] = {55., 67., 97., 124., 140., 166., 163., 129., 52.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Green Brown Terrain
            case kGreenBrownTerrain: {
                double red[] = {0., 33., 73., 124., 136., 152., 159., 171., 223.};
                double green[] = {0., 43., 92., 124., 134., 126., 121., 144., 223.};
                double blue[] = {0., 43., 68., 76., 73., 64., 72., 114., 223.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Green Pink
            case kGreenPink: {
                double red[] = {5., 18., 45., 124., 193., 223., 205., 128., 49.};
                double green[] = {48., 134., 207., 230., 193., 113., 28., 0., 7.};
                double blue[] = {6., 15., 41., 121., 193., 226., 208., 130., 49.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Island
            case kIsland: {
                double red[] = {180., 106., 104., 135., 164., 188., 189., 165., 144.};
                double green[] = {72., 126., 154., 184., 198., 207., 205., 190., 179.};
                double blue[] = {41., 120., 158., 188., 194., 181., 145., 100., 62.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Lake
            case kLake: {
                double red[] = {57., 72., 94., 117., 136., 154., 174., 192., 215.};
                double green[] = {0., 33., 68., 109., 140., 171., 192., 196., 209.};
                double blue[] = {116., 137., 173., 201., 200., 201., 203., 190., 187.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Light Temperature
            case kLightTemperature: {
                double red[] = {31., 71., 123., 160., 210., 222., 214., 199., 183.};
                double green[] = {40., 117., 171., 211., 231., 220., 190., 132., 65.};
                double blue[] = {234., 214., 228., 222., 210., 160., 105., 60., 34.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Light Terrain
            case kLightTerrain: {
                double red[] = {123., 108., 109., 126., 154., 172., 188., 196., 218.};
                double green[] = {184., 138., 130., 133., 154., 175., 188., 196., 218.};
                double blue[] = {208., 130., 109., 99., 110., 122., 150., 171., 218.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Mint
            case kMint: {
                double red[] = {105., 106., 122., 143., 159., 172., 176., 181., 207.};
                double green[] = {252., 197., 194., 187., 174., 162., 153., 136., 125.};
                double blue[] = {146., 133., 144., 155., 163., 167., 166., 162., 174.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Neon
            case kNeon: {
                double red[] = {171., 141., 145., 152., 154., 159., 163., 158., 177.};
                double green[] = {236., 143., 100., 63., 53., 55., 44., 31., 6.};
                double blue[] = {59., 48., 46., 44., 42., 54., 82., 112., 179.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Pastel
            case kPastel: {
                double red[] = {180., 190., 209., 223., 204., 228., 205., 152., 91.};
                double green[] = {93., 125., 147., 172., 181., 224., 233., 198., 158.};
                double blue[] = {236., 218., 160., 133., 114., 132., 162., 220., 218.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Pearl
            case kPearl: {
                double red[] = {225., 183., 162., 135., 115., 111., 119., 145., 211.};
                double green[] = {205., 177., 166., 135., 124., 117., 117., 132., 172.};
                double blue[] = {186., 165., 155., 135., 126., 130., 150., 178., 226.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Pigeon
            case kPigeon: {
                double red[] = {39., 43., 59., 63., 80., 116., 153., 177., 223.};
                double green[] = {39., 43., 59., 74., 91., 114., 139., 165., 223.};
                double blue[] = {39., 50., 59., 70., 85., 115., 151., 176., 223.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Plum
            case kPlum: {
                double red[] = {0., 38., 60., 76., 84., 89., 101., 128., 204.};
                double green[] = {0., 10., 15., 23., 35., 57., 83., 123., 199.};
                double blue[] = {0., 11., 22., 40., 63., 86., 97., 94., 85.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Red Blue
            case kRedBlue: {
                double red[] = {94., 112., 141., 165., 167., 140., 91., 49., 27.};
                double green[] = {27., 46., 88., 135., 166., 161., 135., 97., 58.};
                double blue[] = {42., 52., 81., 106., 139., 158., 155., 137., 116.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Rose
            case kRose: {
                double red[] = {30., 49., 79., 117., 135., 151., 146., 138., 147.};
                double green[] = {63., 60., 72., 90., 94., 94., 68., 46., 16.};
                double blue[] = {18., 28., 41., 56., 62., 63., 50., 36., 21.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Rust
            case kRust: {
                double red[] = {0., 30., 63., 101., 143., 152., 169., 187., 230.};
                double green[] = {0., 14., 28., 42., 58., 61., 67., 74., 91.};
                double blue[] = {39., 26., 21., 18., 15., 14., 14., 13., 13.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Sandy Terrain
            case kSandyTerrain: {
                double red[] = {149., 140., 164., 179., 182., 181., 131., 87., 61.};
                double green[] = {62., 70., 107., 136., 144., 138., 117., 87., 74.};
                double blue[] = {40., 38., 45., 49., 49., 49., 38., 32., 34.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Sienna
            case kSienna: {
                double red[] = {99., 112., 148., 165., 179., 182., 183., 183., 208.};
                double green[] = {39., 40., 57., 79., 104., 127., 148., 161., 198.};
                double blue[] = {15., 16., 18., 33., 51., 79., 103., 129., 177.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Solar
            case kSolar: {
                double red[] = {99., 116., 154., 174., 200., 196., 201., 201., 230.};
                double green[] = {0., 0., 8., 32., 58., 83., 119., 136., 173.};
                double blue[] = {5., 6., 7., 9., 9., 14., 17., 19., 24.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // South West
            case kSouthWest: {
                double red[] = {82., 106., 126., 141., 155., 163., 142., 107., 66.};
                double green[] = {62., 44., 69., 107., 135., 152., 149., 132., 119.};
                double blue[] = {39., 25., 31., 60., 73., 68., 49., 72., 188.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Starry Night
            case kStarryNight: {
                double red[] = {18., 29., 44., 72., 116., 158., 184., 208., 221.};
                double green[] = {27., 46., 71., 105., 146., 177., 189., 190., 183.};
                double blue[] = {39., 55., 80., 108., 130., 133., 124., 100., 76.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Sunset
            case kSunset: {
                double red[] = {0., 48., 119., 173., 212., 224., 228., 228., 245.};
                double green[] = {0., 13., 30., 47., 79., 127., 167., 205., 245.};
                double blue[] = {0., 68., 75., 43., 16., 22., 55., 128., 245.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Temperature Map
            case kTemperatureMap: {
                double red[] = {34., 70., 129., 187., 225., 226., 216., 193., 179.};
                double green[] = {48., 91., 147., 194., 226., 229., 196., 110., 12.};
                double blue[] = {234., 212., 216., 224., 206., 110., 53., 40., 29.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Thermometer
            case kThermometer: {
                double red[] = {30., 55., 103., 147., 174., 203., 188., 151., 105.};
                double green[] = {0., 65., 138., 182., 187., 175., 121., 53., 9.};
                double blue[] = {191., 202., 212., 208., 171., 140., 97., 57., 30.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Valentine
            case kValentine: {
                double red[] = {112., 97., 113., 125., 138., 159., 178., 188., 225.};
                double green[] = {16., 17., 24., 37., 56., 81., 110., 136., 189.};
                double blue[] = {38., 35., 46., 59., 78., 103., 130., 152., 201.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Visible Spectrum
            case kVisibleSpectrum: {
                double red[] = {18., 72., 5., 23., 29., 201., 200., 98., 29.};
                double green[] = {0., 0., 43., 167., 211., 117., 0., 0., 0.};
                double blue[] = {51., 203., 177., 26., 10., 9., 8., 3., 0.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Water Melon
            case kWaterMelon: {
                double red[] = {19., 42., 64., 88., 118., 147., 175., 187., 205.};
                double green[] = {19., 55., 89., 125., 154., 169., 161., 129., 70.};
                double blue[] = {19., 32., 47., 70., 100., 128., 145., 130., 75.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Cool
            case kCool: {
                double red[] = {33., 31., 42., 68., 86., 111., 141., 172., 227.};
                double green[] = {255., 175., 145., 106., 88., 55., 15., 0., 0.};
                double blue[] = {255., 205., 202., 203., 208., 205., 203., 206., 231.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Copper
            case kCopper: {
                double red[] = {0., 25., 50., 79., 110., 145., 181., 201., 254.};
                double green[] = {0., 16., 30., 46., 63., 82., 101., 124., 179.};
                double blue[] = {0., 12., 21., 29., 39., 49., 61., 74., 103.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Gist Earth
            case kGistEarth: {
                double red[] = {0., 13., 30., 44., 72., 120., 156., 200., 247.};
                double green[] = {0., 36., 84., 117., 141., 153., 151., 158., 247.};
                double blue[] = {0., 94., 100., 82., 56., 66., 76., 131., 247.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            // Viridis
            case kViridis: {
                double red[] = {26., 51., 43., 33., 28., 35., 74., 144., 246.};
                double green[] = {9., 24., 55., 87., 118., 150., 180., 200., 222.};
                double blue[] = {30., 96., 112., 114., 112., 101., 72., 35., 0.};
                CreateGradientColorTable(red, green, blue);
            }
            break;

            default:
                throw new UnsupportedOperationException("Unknown palette number");
        }

    }
}
