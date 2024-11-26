# twig

twig is the reincarnation of groot and is the project that is actively maintained. Please visit the 
[twig repository](https://github.com/gavalian/twig) to get the package.

# Data Visualization and Analysis Software

Powerfull data analysis and visualization tool writte in pure Java. Can be included in the application.
Twig library is evolution of groot, which was initially developped for small data visualization while developing data reconstruction codes,
and since became very improtant part of CLAS12 online and offline software.This project is actively developped.

# Visualization

<img src="https://github.com/gavalian/twig/blob/main/tutorials/images/twig-demo-0.0.4.png" width="900">

Examples produced by [twig repository](https://github.com/gavalian/twig)

# Usage

Include twig project in your pom using:

```html
 <!-- github repository for TWIG Library -->
 <repositories>
    <repository>
      <id>twig-github</id>
      <url>https://maven.pkg.github.com/gavalian/twig</url>
    </repository>
 </repositories>
<!-- TWIG Library (Java Data Visualization and Analysis) -->
<dependency>
  <groupId>j4np</groupId>
  <artifactId>twig</artifactId>
  <version>0.0.4</version>
</dependency>
```

Check out the distribution site for newer versions: (click on "Packages" on the right menu bar)

# Tutorials

The tutorials for some of the graph types can be found in directory tutorials/plotting, and direcotry 
tutorials/io containds examples of data IO., such as saving histograms and reading and plotting, also 
reading data from CSV and Text files and plotting graphs.

To run tutorials compile the library:

```
prompt> git clone https://github.com/gavalian/twig.git
prompt> mvn install
```

then run command (depnding what version you got)

```
prompt> jshell jshell --class-path target/twig-0.0.4-core.jar --startup etc/imports.jshell tutorials/plotting/advanced_bar_chart.java
```
the example scripts are writte to run in JSHELL, if you'd like to include them in your Java program, you must add the imports found in
etc/imports.jshell file.
If you'd like a specific example of your favorite graph type, please, submit an issue and I will create the desired example.

# Gallery

Gallery from [twig repository](https://github.com/gavalian/twig)

<table class="center" width="100%">
    <tr>
        <td width="50%">
            <figure>
                <a href="https://github.com/gavalian/twig/blob/main/tutorials/plotting/advanced_graph_fitting.java">
                 <img src="https://github.com/gavalian/twig/blob/main/tutorials/images/figure_advanced_graph_fitting.png" alt=""></a>
                <figcaption><h2></h2></figcaption>
            </figure>
        </td>
        <td width="50%">
             <a href="https://github.com/gavalian/twig/blob/main/tutorials/plotting/advanced_bar_chart.java">
              <img src="https://github.com/gavalian/twig/blob/main/tutorials/images/figure_advanced_bar_chart.png" alt=""></a>
                <figcaption><h2></h2></figcaption>
        </td>
    </tr>   
 <tr>       
   <td width="50%">
            <figure>
                <a href="https://github.com/gavalian/twig/blob/main/tutorials/plotting/confusion_matrix.java">
                 <img src="https://github.com/gavalian/twig/blob/main/tutorials/images/figure_confusion_matrix.png" alt=""></a>
                <figcaption><h2></h2></figcaption>
            </figure>
        </td>
        <td width="50%">
             <a href="https://github.com/gavalian/twig/blob/main/tutorials/images/figure_slice_graph_3d.png">
              <img src="https://github.com/gavalian/twig/blob/main/tutorials/images/figure_slice_graph_3d.png" alt=""></a>
                <figcaption><h2></h2></figcaption>
        </td>
    </tr>
</table>


# Example of creating tuple from text file (Appendix)

We start from file that constains events each presented in two lines

```
awk '{print $2,$5,$6,$7}' extractedDataPred2.txt > epip_hb.txt
```

this command takes two lines from the input and joins them into one line

```
paste - - < epip_hb.txt > epip_hb_joined.txt
```

