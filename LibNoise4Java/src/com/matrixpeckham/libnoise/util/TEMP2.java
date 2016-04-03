/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.util;

/**
 *
 * @author William Matrix Peckham
 */
public class TEMP2 {

}

// Bitmap header size.
const int BMP_HEADER_SIZE = 54;

// Direction of the light source, in compass degrees (0 = north, 90 = east,
// 180 = south, 270 = east)
const double DEFAULT_LIGHT_AZIMUTH = 45.0;

// Amount of contrast between light and dark areas.
const double DEFAULT_LIGHT_CONTRAST  =  1.0;

// Elevation of the light source above the horizon, in degrees (0 = on
// horizon, 90 = directly overhead)
const double DEFAULT_LIGHT_ELEVATION = 45.0;




/////////////////////////////////////////////////////////////////////////////
// NoiseMapBuilder class

NoiseMapBuilder::NoiseMapBuilder ():
  m_pCallback (NULL),
  m_destHeight (0),
  m_destWidth  (0),
  m_pDestNoiseMap (NULL),
  m_pSourceModule (NULL)
{
}

void NoiseMapBuilder::SetCallback (NoiseMapCallback pCallback)
{
  m_pCallback = pCallback;
}

/////////////////////////////////////////////////////////////////////////////
// NoiseMapBuilderCylinder class

NoiseMapBuilderCylinder::NoiseMapBuilderCylinder ():
  m_lowerAngleBound  (0.0),
  m_lowerHeightBound (0.0),
  m_upperAngleBound  (0.0),
  m_upperHeightBound (0.0)
{
}

void NoiseMapBuilderCylinder::Build ()
{
  if ( m_upperAngleBound <= m_lowerAngleBound
    || m_upperHeightBound <= m_lowerHeightBound
    || m_destWidth <= 0
    || m_destHeight <= 0
    || m_pSourceModule == NULL
    || m_pDestNoiseMap == NULL) {
    throw noise::ExceptionInvalidParam ();
  }

  // Resize the destination noise map so that it can store the new output
  // values from the source model.
  m_pDestNoiseMap->SetSize (m_destWidth, m_destHeight);

  // Create the cylinder model.
  model::Cylinder cylinderModel;
  cylinderModel.SetModule (*m_pSourceModule);

  double angleExtent  = m_upperAngleBound  - m_lowerAngleBound ;
  double heightExtent = m_upperHeightBound - m_lowerHeightBound;
  double xDelta = angleExtent  / (double)m_destWidth ;
  double yDelta = heightExtent / (double)m_destHeight;
  double curAngle  = m_lowerAngleBound ;
  double curHeight = m_lowerHeightBound;

  // Fill every point in the noise map with the output values from the model.
  for (int y = 0; y < m_destHeight; y++) {
    float* pDest = m_pDestNoiseMap->GetSlabPtr (y);
    curAngle = m_lowerAngleBound;
    for (int x = 0; x < m_destWidth; x++) {
      float curValue = (float)cylinderModel.GetValue (curAngle, curHeight);
      *pDest++ = curValue;
      curAngle += xDelta;
    }
    curHeight += yDelta;
    if (m_pCallback != NULL) {
      m_pCallback (y);
    }
  }
}

/////////////////////////////////////////////////////////////////////////////
// NoiseMapBuilderPlane class

NoiseMapBuilderPlane::NoiseMapBuilderPlane ():
  m_isSeamlessEnabled (false),
  m_lowerXBound  (0.0),
  m_lowerZBound  (0.0),
  m_upperXBound  (0.0),
  m_upperZBound  (0.0)
{
}

void NoiseMapBuilderPlane::Build ()
{
  if ( m_upperXBound <= m_lowerXBound
    || m_upperZBound <= m_lowerZBound
    || m_destWidth <= 0
    || m_destHeight <= 0
    || m_pSourceModule == NULL
    || m_pDestNoiseMap == NULL) {
    throw noise::ExceptionInvalidParam ();
  }

  // Resize the destination noise map so that it can store the new output
  // values from the source model.
  m_pDestNoiseMap->SetSize (m_destWidth, m_destHeight);

  // Create the plane model.
  model::Plane planeModel;
  planeModel.SetModule (*m_pSourceModule);

  double xExtent = m_upperXBound - m_lowerXBound;
  double zExtent = m_upperZBound - m_lowerZBound;
  double xDelta  = xExtent / (double)m_destWidth ;
  double zDelta  = zExtent / (double)m_destHeight;
  double xCur    = m_lowerXBound;
  double zCur    = m_lowerZBound;

  // Fill every point in the noise map with the output values from the model.
  for (int z = 0; z < m_destHeight; z++) {
    float* pDest = m_pDestNoiseMap->GetSlabPtr (z);
    xCur = m_lowerXBound;
    for (int x = 0; x < m_destWidth; x++) {
      float finalValue;
      if (!m_isSeamlessEnabled) {
        finalValue = planeModel.GetValue (xCur, zCur);
      } else {
        double swValue, seValue, nwValue, neValue;
        swValue = planeModel.GetValue (xCur          , zCur          );
        seValue = planeModel.GetValue (xCur + xExtent, zCur          );
        nwValue = planeModel.GetValue (xCur          , zCur + zExtent);
        neValue = planeModel.GetValue (xCur + xExtent, zCur + zExtent);
        double xBlend = 1.0 - ((xCur - m_lowerXBound) / xExtent);
        double zBlend = 1.0 - ((zCur - m_lowerZBound) / zExtent);
        double z0 = LinearInterp (swValue, seValue, xBlend);
        double z1 = LinearInterp (nwValue, neValue, xBlend);
        finalValue = (float)LinearInterp (z0, z1, zBlend);
      }
      *pDest++ = finalValue;
      xCur += xDelta;
    }
    zCur += zDelta;
    if (m_pCallback != NULL) {
      m_pCallback (z);
    }
  }
}

/////////////////////////////////////////////////////////////////////////////
// NoiseMapBuilderSphere class

NoiseMapBuilderSphere::NoiseMapBuilderSphere ():
  m_eastLonBound  (0.0),
  m_northLatBound (0.0),
  m_southLatBound (0.0),
  m_westLonBound  (0.0)
{
}

void NoiseMapBuilderSphere::Build ()
{
  if ( m_eastLonBound <= m_westLonBound
    || m_northLatBound <= m_southLatBound
    || m_destWidth <= 0
    || m_destHeight <= 0
    || m_pSourceModule == NULL
    || m_pDestNoiseMap == NULL) {
    throw noise::ExceptionInvalidParam ();
  }

  // Resize the destination noise map so that it can store the new output
  // values from the source model.
  m_pDestNoiseMap->SetSize (m_destWidth, m_destHeight);

  // Create the plane model.
  model::Sphere sphereModel;
  sphereModel.SetModule (*m_pSourceModule);

  double lonExtent = m_eastLonBound  - m_westLonBound ;
  double latExtent = m_northLatBound - m_southLatBound;
  double xDelta = lonExtent / (double)m_destWidth ;
  double yDelta = latExtent / (double)m_destHeight;
  double curLon = m_westLonBound ;
  double curLat = m_southLatBound;

  // Fill every point in the noise map with the output values from the model.
  for (int y = 0; y < m_destHeight; y++) {
    float* pDest = m_pDestNoiseMap->GetSlabPtr (y);
    curLon = m_westLonBound;
    for (int x = 0; x < m_destWidth; x++) {
      float curValue = (float)sphereModel.GetValue (curLat, curLon);
      *pDest++ = curValue;
      curLon += xDelta;
    }
    curLat += yDelta;
    if (m_pCallback != NULL) {
      m_pCallback (y);
    }
  }
}

//////////////////////////////////////////////////////////////////////////////
// RendererImage class

RendererImage::RendererImage ():
  m_isLightEnabled    (false),
  m_isWrapEnabled     (false),
  m_lightAzimuth      (45.0),
  m_lightBrightness   (1.0),
  m_lightColor        (255, 255, 255, 255),
  m_lightContrast     (1.0),
  m_lightElev         (45.0),
  m_lightIntensity    (1.0),
  m_pBackgroundImage  (NULL),
  m_pDestImage        (NULL),
  m_pSourceNoiseMap   (NULL),
  m_recalcLightValues (true)
{
  BuildGrayscaleGradient ();
};

void RendererImage::AddGradientPoint (double gradientPos,
  const Color& gradientColor)
{
  m_gradient.AddGradientPoint (gradientPos, gradientColor);
}

void RendererImage::BuildGrayscaleGradient ()
{
  ClearGradient ();
  m_gradient.AddGradientPoint (-1.0, Color (  0,   0,   0, 255));
  m_gradient.AddGradientPoint ( 1.0, Color (255, 255, 255, 255));
}

void RendererImage::BuildTerrainGradient ()
{
  ClearGradient ();
  m_gradient.AddGradientPoint (-1.00, Color (  0,   0, 128, 255));
  m_gradient.AddGradientPoint (-0.20, Color ( 32,  64, 128, 255));
  m_gradient.AddGradientPoint (-0.04, Color ( 64,  96, 192, 255));
  m_gradient.AddGradientPoint (-0.02, Color (192, 192, 128, 255));
  m_gradient.AddGradientPoint ( 0.00, Color (  0, 192,   0, 255));
  m_gradient.AddGradientPoint ( 0.25, Color (192, 192,   0, 255));
  m_gradient.AddGradientPoint ( 0.50, Color (160,  96,  64, 255));
  m_gradient.AddGradientPoint ( 0.75, Color (128, 255, 255, 255));
  m_gradient.AddGradientPoint ( 1.00, Color (255, 255, 255, 255));
}

Color RendererImage::CalcDestColor (const Color& sourceColor,
  const Color& backgroundColor, double lightValue) const
{
  double sourceRed   = (double)sourceColor.red   / 255.0;
  double sourceGreen = (double)sourceColor.green / 255.0;
  double sourceBlue  = (double)sourceColor.blue  / 255.0;
  double sourceAlpha = (double)sourceColor.alpha / 255.0;
  double backgroundRed   = (double)backgroundColor.red   / 255.0;
  double backgroundGreen = (double)backgroundColor.green / 255.0;
  double backgroundBlue  = (double)backgroundColor.blue  / 255.0;

  // First, blend the source color to the background color using the alpha
  // of the source color.
  double red   = LinearInterp (backgroundRed,   sourceRed  , sourceAlpha);
  double green = LinearInterp (backgroundGreen, sourceGreen, sourceAlpha);
  double blue  = LinearInterp (backgroundBlue,  sourceBlue , sourceAlpha);

  if (m_isLightEnabled) {

    // Now calculate the light color.
    double lightRed   = lightValue * (double)m_lightColor.red   / 255.0;
    double lightGreen = lightValue * (double)m_lightColor.green / 255.0;
    double lightBlue  = lightValue * (double)m_lightColor.blue  / 255.0;

    // Apply the light color to the new color.
    red   *= lightRed  ;
    green *= lightGreen;
    blue  *= lightBlue ;
  }

  // Clamp the color channels to the (0..1) range.
  red   = (red   < 0.0)? 0.0: red  ;
  red   = (red   > 1.0)? 1.0: red  ;
  green = (green < 0.0)? 0.0: green;
  green = (green > 1.0)? 1.0: green;
  blue  = (blue  < 0.0)? 0.0: blue ;
  blue  = (blue  > 1.0)? 1.0: blue ;

  // Rescale the color channels to the noise::uint8 (0..255) range and return
  // the new color.
  Color newColor (
    (noise::uint8)((noise::uint)(red   * 255.0) & 0xff),
    (noise::uint8)((noise::uint)(green * 255.0) & 0xff),
    (noise::uint8)((noise::uint)(blue  * 255.0) & 0xff),
    GetMax (sourceColor.alpha, backgroundColor.alpha));
  return newColor;
}

double RendererImage::CalcLightIntensity (double center, double left,
  double right, double down, double up) const
{
  // Recalculate the sine and cosine of the various light values if
  // necessary so it does not have to be calculated each time this method is
  // called.
  if (m_recalcLightValues) {
    m_cosAzimuth = cos (m_lightAzimuth * DEG_TO_RAD);
    m_sinAzimuth = sin (m_lightAzimuth * DEG_TO_RAD);
    m_cosElev    = cos (m_lightElev    * DEG_TO_RAD);
    m_sinElev    = sin (m_lightElev    * DEG_TO_RAD);
    m_recalcLightValues = false;
  }

  // Now do the lighting calculations.
  const double I_MAX = 1.0;
  double io = I_MAX * SQRT_2 * m_sinElev / 2.0;
  double ix = (I_MAX - io) * m_lightContrast * SQRT_2 * m_cosElev
    * m_cosAzimuth;
  double iy = (I_MAX - io) * m_lightContrast * SQRT_2 * m_cosElev
    * m_sinAzimuth;
  double intensity = (ix * (left - right) + iy * (down - up) + io);
  if (intensity < 0.0) {
    intensity = 0.0;
  }
  return intensity;
}

void RendererImage::ClearGradient ()
{
  m_gradient.Clear ();
}

void RendererImage::Render ()
{
  if ( m_pSourceNoiseMap == NULL
    || m_pDestImage == NULL
    || m_pSourceNoiseMap->GetWidth  () <= 0
    || m_pSourceNoiseMap->GetHeight () <= 0
    || m_gradient.GetGradientPointCount () < 2) {
    throw noise::ExceptionInvalidParam ();
  }

  int width  = m_pSourceNoiseMap->GetWidth  ();
  int height = m_pSourceNoiseMap->GetHeight ();

  // If a background image was provided, make sure it is the same size the
  // source noise map.
  if (m_pBackgroundImage != NULL) {
    if ( m_pBackgroundImage->GetWidth  () != width
      || m_pBackgroundImage->GetHeight () != height) {
      throw noise::ExceptionInvalidParam ();
    }
  }

  // Create the destination image.  It is safe to reuse it if this is also the
  // background image.
  if (m_pDestImage != m_pBackgroundImage) {
    m_pDestImage->SetSize (width, height);
  }

  for (int y = 0; y < height; y++) {
    const Color* pBackground = NULL;
    if (m_pBackgroundImage != NULL) {
      pBackground = m_pBackgroundImage->GetConstSlabPtr (y);
    }
    const float* pSource = m_pSourceNoiseMap->GetConstSlabPtr (y);
    Color* pDest = m_pDestImage->GetSlabPtr (y);
    for (int x = 0; x < width; x++) {

      // Get the color based on the value at the current point in the noise
      // map.
      Color destColor = m_gradient.GetColor (*pSource);

      // If lighting is enabled, calculate the light intensity based on the
      // rate of change at the current point in the noise map.
      double lightIntensity;
      if (m_isLightEnabled) {

        // Calculate the positions of the current point's four-neighbors.
        int xLeftOffset, xRightOffset;
        int yUpOffset  , yDownOffset ;
        if (m_isWrapEnabled) {
          if (x == 0) {
            xLeftOffset  = (int)width - 1;
            xRightOffset = 1;
          } else if (x == (int)width - 1) {
            xLeftOffset  = -1;
            xRightOffset = -((int)width - 1);
          } else {
            xLeftOffset  = -1;
            xRightOffset = 1;
          }
          if (y == 0) {
            yDownOffset = (int)height - 1;
            yUpOffset   = 1;
          } else if (y == (int)height - 1) {
            yDownOffset = -1;
            yUpOffset   = -((int)height - 1);
          } else {
            yDownOffset = -1;
            yUpOffset   = 1;
          }
        } else {
          if (x == 0) {
            xLeftOffset  = 0;
            xRightOffset = 1;
          } else if (x == (int)width - 1) {
            xLeftOffset  = -1;
            xRightOffset = 0;
          } else {
            xLeftOffset  = -1;
            xRightOffset = 1;
          }
          if (y == 0) {
            yDownOffset = 0;
            yUpOffset   = 1;
          } else if (y == (int)height - 1) {
            yDownOffset = -1;
            yUpOffset   = 0;
          } else {
            yDownOffset = -1;
            yUpOffset   = 1;
          }
        }
        yDownOffset *= m_pSourceNoiseMap->GetStride ();
        yUpOffset   *= m_pSourceNoiseMap->GetStride ();

        // Get the noise value of the current point in the source noise map
        // and the noise values of its four-neighbors.
        double nc = (double)(*pSource);
        double nl = (double)(*(pSource + xLeftOffset ));
        double nr = (double)(*(pSource + xRightOffset));
        double nd = (double)(*(pSource + yDownOffset ));
        double nu = (double)(*(pSource + yUpOffset   ));

        // Now we can calculate the lighting intensity.
        lightIntensity = CalcLightIntensity (nc, nl, nr, nd, nu);
        lightIntensity *= m_lightBrightness;

      } else {

        // These values will apply no lighting to the destination image.
        lightIntensity = 1.0;
      }

      // Get the current background color from the background image.
      Color backgroundColor (255, 255, 255, 255);
      if (m_pBackgroundImage != NULL) {
        backgroundColor = *pBackground;
      }

      // Blend the destination color, background color, and the light
      // intensity together, then update the destination image with that
      // color.
      *pDest = CalcDestColor (destColor, backgroundColor, lightIntensity);

      // Go to the next point.
      ++pSource;
      ++pDest;
      if (m_pBackgroundImage != NULL) {
        ++pBackground;
      }
    }
  }
}

//////////////////////////////////////////////////////////////////////////////
// RendererNormalMap class

RendererNormalMap::RendererNormalMap ():
  m_bumpHeight      (1.0),
  m_isWrapEnabled   (false),
  m_pDestImage      (NULL),
  m_pSourceNoiseMap (NULL)
{
};

Color RendererNormalMap::CalcNormalColor (double nc, double nr, double nu,
  double bumpHeight) const
{
  // Calculate the surface normal.
  nc *= bumpHeight;
  nr *= bumpHeight;
  nu *= bumpHeight;
  double ncr = (nc - nr);
  double ncu = (nc - nu);
  double d = sqrt ((ncu * ncu) + (ncr * ncr) + 1);
  double vxc = (nc - nr) / d;
  double vyc = (nc - nu) / d;
  double vzc = 1.0 / d;

  // Map the normal range from the (-1.0 .. +1.0) range to the (0 .. 255)
  // range.
  noise::uint8 xc, yc, zc;
  xc = (noise::uint8)((noise::uint)((floor)((vxc + 1.0) * 127.5)) & 0xff);
  yc = (noise::uint8)((noise::uint)((floor)((vyc + 1.0) * 127.5)) & 0xff);
  zc = (noise::uint8)((noise::uint)((floor)((vzc + 1.0) * 127.5)) & 0xff);

  return Color (xc, yc, zc, 0);
}

void RendererNormalMap::Render ()
{
  if ( m_pSourceNoiseMap == NULL
    || m_pDestImage == NULL
    || m_pSourceNoiseMap->GetWidth  () <= 0
    || m_pSourceNoiseMap->GetHeight () <= 0) {
    throw noise::ExceptionInvalidParam ();
  }

  int width  = m_pSourceNoiseMap->GetWidth  ();
  int height = m_pSourceNoiseMap->GetHeight ();

  for (int y = 0; y < height; y++) {
    const float* pSource = m_pSourceNoiseMap->GetConstSlabPtr (y);
    Color* pDest = m_pDestImage->GetSlabPtr (y);
    for (int x = 0; x < width; x++) {

      // Calculate the positions of the current point's right and up
      // neighbors.
      int xRightOffset, yUpOffset;
      if (m_isWrapEnabled) {
        if (x == (int)width - 1) {
          xRightOffset = -((int)width - 1);
        } else {
          xRightOffset = 1;
        }
        if (y == (int)height - 1) {
          yUpOffset = -((int)height - 1);
        } else {
          yUpOffset = 1;
        }
      } else {
        if (x == (int)width - 1) {
          xRightOffset = 0;
        } else {
          xRightOffset = 1;
        }
        if (y == (int)height - 1) {
          yUpOffset = 0;
        } else {
          yUpOffset = 1;
        }
      }
      yUpOffset *= m_pSourceNoiseMap->GetStride ();

      // Get the noise value of the current point in the source noise map
      // and the noise values of its right and up neighbors.
      double nc = (double)(*pSource);
      double nr = (double)(*(pSource + xRightOffset));
      double nu = (double)(*(pSource + yUpOffset   ));

      // Calculate the normal product.
      *pDest = CalcNormalColor (nc, nr, nu, m_bumpHeight);

      // Go to the next point.
      ++pSource;
      ++pDest;
    }
  }
}
