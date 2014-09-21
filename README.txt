AndroidSidebar - README
=======================


About
-----

"AndroidSidebar" is an Android-library, which provides a custom view implementation, which 
allows to show a sidebar, which overlaps the view's main content and can be shown or hidden 
in an animated manner. The sidebar may be located at left or right edge of the parent view 
and its state can be changed by either calling an appropriate method or via dragging on the 
device's touch screen. Furthermore there are a lot of attributes, which allow to specify the
appearance and behavior of the sidebar.

The library provides the following features:

    -   The sidebar can be located at left or right edge of its parent view.
        
    -   The width of the sidebar can be set relatively to the width of its parent view.
        Additionally, a maximum width can be defined.
    
    -   It is possible to specify an offset, which defines the amount of space the sidebar
        is visible even if it currently hidden. The offset can be set relatively to the
        width of the parent view and a maximum value can be defined.

    -   The way, the main content is animated, when the sidebar becomes shown or hidden,
        can be adjusted in many ways, including the possibility to move it or to resize it.

    -   The sidebar's state can be changed via drag gestures or by clicking the touch screen
        of the device. For each possibility multiple settings exist, e.g. it is possible to
        change the sensibility for recognizing drag gestures.

    -   The speed of the animation, which is used to show or hide the sidebar, can be chosen
        freely.

    -   The sidebar may render a semi-transparent shadow in front of the main content, whose
        appearance can be adjusted concerning size and color.

    -   It is possible to fade in a semi-transparent overlay, which is shown in front of the 
        main content, when the sidebar becomes shown. The transparency and color of the
        overlay can be adjusted. 

	
License Agreement
-----------------

AndroidSidebar is distributed under the GNU Lesser Public License version 3.0 (GLPLv3 ). For further 
information about this license agreement's content, please refer to its full version, which is 
available at http://www.gnu.org/licenses/.


Download
--------

The homepage of the project "AndroidSidebar" is available on Sourceforge via the internet address 
https://sourceforge.net/projects/androidsidebar.

The latest release of the project can be downloaded as a zip archive from the download 
section of the Sourceforge project site mentioned above, which is available via the direct 
link https://sourceforge.net/projects/androidsidebar/files.

As well, the complete source code and documentation is available via a Mercurial repository, 
which can be accessed by the URL http://hg.code.sf.net/p/androidsidebar/code.


Contact information
-------------------

For personal feedback or questions you can either contact the developer via his profile on 
Sourceforge, which is available under the direct link https://sourceforge.net/users/mrapp, or 
via the email address michael.rapp90@googlemail.com.