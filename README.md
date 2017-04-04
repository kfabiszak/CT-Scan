# CT-Scan
Tomograph simulator.
<h2> Functions overview </h2>

* Demonstration of the progress of image reconstruction:

> * <code> generateSinogram() </code> (class Sinogram) - creating sinograph and normalization.

> * <code> scan() </code> (class Scan) - recreating output image, saving intermediate stages and normalization.

* Saving image to DICOM format:

> * <code> generateDICOM() </code> (class DICOM).

<h2> Tomograph specification </h2>

Used conical tomograph model and additive radiation emisson model.
Used Bresenham's algorithm to roam through consecutive pixels discrete image.

No filtering added yet.
