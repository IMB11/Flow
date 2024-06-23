#version 150

uniform sampler2D InputSampler;
uniform vec2 InputResolution;
uniform vec4 ColorModulator;

uniform float Directions;
uniform float Quality;
uniform float Size;

out vec4 fragColor;

// from owo-lib
// shader adapted from https://www.shadertoy.com/view/Xltfzj

void main() {
    #define TAU 6.28318530718

    vec2 Radius = Size / InputResolution.xy;

    // Normalized pixel coordinates (from 0 to 1)
    vec2 uv = gl_FragCoord.xy / InputResolution.xy;
    // Pixel colour
    vec4 Color = texture(InputSampler, uv);

    // Calculate total number of samples
    int totalSamples = int(Directions * Quality);

    // Single loop for blur calculations
    for (int sample = 0; sample < totalSamples; ++sample) {
        float d = (float(sample) / Quality) * (TAU / Directions);
        float i = (mod(float(sample), Quality) + 1.0) / Quality;
        Color += texture(InputSampler, uv + vec2(cos(d), sin(d)) * Radius * i);
    }

    // Output to screen
    Color /= Quality * Directions;
    fragColor = Color * ColorModulator;
}