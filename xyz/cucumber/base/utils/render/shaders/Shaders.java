package xyz.cucumber.base.utils.render.shaders;

import xyz.cucumber.base.utils.render.Shader;

public class Shaders {
	
	public static Shader outline = new Shader("#version 120\r\n"
			+ "\r\n"
			+ "uniform vec2 texelSize, direction;\r\n"
			+ "uniform sampler2D texture;\r\n"
			+ "uniform float radius;\r\n"
			+ "uniform vec3 color;\r\n"
			+ "\r\n"
			+ "#define offset direction * texelSize\r\n"
			+ "\r\n"
			+ "void main() {\r\n"
			+ "    float centerAlpha = texture2D(texture, gl_TexCoord[0].xy).a;\r\n"
			+ "    float innerAlpha = centerAlpha;\r\n"
			+ "    for (float r = 1.0; r <= radius; r++) {\r\n"
			+ "        float alphaCurrent1 = texture2D(texture, gl_TexCoord[0].xy + offset * r).a;\r\n"
			+ "        float alphaCurrent2 = texture2D(texture, gl_TexCoord[0].xy - offset * r).a;\r\n"
			+ "\r\n"
			+ "       innerAlpha += alphaCurrent1 + alphaCurrent2;\r\n"
			+ "    }\r\n"
			+ "\r\n"
			+ "    gl_FragColor = vec4(color, innerAlpha) * step(0.0, -centerAlpha);\r\n"
			+ "}");
	public static Shader testShader = new Shader("#version 120\r\n"
			+ "\r\n"
			+ "uniform float iTime;\r\n"
			+ "\r\n"
			+ "float gradient(float p)\r\n"
			+ "{\r\n"
			+ "    vec2 pt0 = vec2(0.00,0.0);\r\n"
			+ "    vec2 pt1 = vec2(0.86,0.1);\r\n"
			+ "    vec2 pt2 = vec2(0.955,0.40);\r\n"
			+ "    vec2 pt3 = vec2(0.99,1.0);\r\n"
			+ "    vec2 pt4 = vec2(1.00,0.0);\r\n"
			+ "    if (p < pt0.x) return pt0.y;\r\n"
			+ "    if (p < pt1.x) return mix(pt0.y, pt1.y, (p-pt0.x) / (pt1.x-pt0.x));\r\n"
			+ "    if (p < pt2.x) return mix(pt1.y, pt2.y, (p-pt1.x) / (pt2.x-pt1.x));\r\n"
			+ "    if (p < pt3.x) return mix(pt2.y, pt3.y, (p-pt2.x) / (pt3.x-pt2.x));\r\n"
			+ "    if (p < pt4.x) return mix(pt3.y, pt4.y, (p-pt3.x) / (pt4.x-pt3.x));\r\n"
			+ "    return pt4.y;\r\n"
			+ "}\r\n"
			+ "\r\n"
			+ "float waveN(vec2 uv, vec2 s12, vec2 t12, vec2 f12, vec2 h12)\r\n"
			+ "{\r\n"
			+ "vec2 x12 = sin((iTime * s12 + t12 + uv.x) * f12) * h12;\r\n"
			+ "\r\n"
			+ "    float g = gradient(uv.y / (0.2 + x12.x + x12.y));\r\n"
			+ "    \r\n"
			+ "	return g * 0.5;"
			+ "}\r\n"
			+ "\r\n"
			+ "float wave1(vec2 uv)\r\n"
			+ "{\r\n"
			+ "    return waveN(vec2(uv.x,uv.y-0.25), vec2(0.03,0.06), vec2(0.00,0.02), vec2(8.0,3.7), vec2(0.06,0.05));\r\n"
			+ "}\r\n"
			+ "\r\n"
			+ "float wave2(vec2 uv)\r\n"
			+ "{\r\n"
			+ "    return waveN(vec2(uv.x,uv.y-0.25), vec2(0.04,0.07), vec2(0.16,-0.37), vec2(6.7,2.89), vec2(0.06,0.05));\r\n"
			+ "}\r\n"
			+ "\r\n"
			+ "float wave3(vec2 uv)\r\n"
			+ "{\r\n"
			+ "    return waveN(vec2(uv.x,0.75-uv.y), vec2(0.035,0.055), vec2(-0.09,0.27), vec2(7.4,2.51), vec2(0.06,0.05));\r\n"
			+ "}\r\n"
			+ "\r\n"
			+ "float wave4(vec2 uv)\r\n"
			+ "{\r\n"
			+ "    return waveN(vec2(uv.x,0.75-uv.y), vec2(0.032,0.09), vec2(0.08,-0.22), vec2(6.5,3.89), vec2(0.06,0.05));\r\n"
			+ "}\r\n"
			+ "\r\n"
			+ "void main()\r\n"
			+ "{\r\n"
			+ "    vec2 uv = gl_TexCoord[0].st;\r\n"
			+ "    \r\n"
			+ "    float waves = wave1(uv) + wave2(uv) + wave3(uv) + wave4(uv);\r\n"
			+ "    \r\n"
			+ "	float x = uv.x;\r\n"
			+ "float y = 0.5 - uv.y;\r\n"
			+ "    \r\n"
			+ "    vec3 bg = mix(vec3(0.03,0.05,0.3),vec3(0.1,0.25,0.45), (x+y)*0.5);\r\n"
			+ "    vec3 ac = bg + vec3(.6,1.0,1.) * waves;"
			+ "\r\n"
			+ "    gl_FragColor = vec4(ac, 1.0);\r\n"
			+ "}");
	public static Shader bloom = new Shader("#version 120\r\n"
			+ "\r\n"
			+ "	uniform sampler2D u_diffuse_sampler;\r\n"
			+ "	uniform sampler2D u_other_sampler;\r\n"
			+ "	uniform vec2 u_texel_size;\r\n"
			+ "	uniform vec2 u_direction;\r\n"
			+ "	uniform float u_radius;\r\n"
			+ "	uniform float u_kernel[128];\r\n"
			+ "\r\n"
			+ "	void main(void)\r\n"
			+ "	{\r\n"
			+ "	    vec2 uv = gl_TexCoord[0].st;\r\n"
			+ "\r\n"
			+ "	    if (u_direction.x == 0.0) {\r\n"
			+ "	        float alpha = texture2D(u_other_sampler, uv).a;\r\n"
			+ "	        if (alpha > 0) discard;\r\n"
			+ "	    }\r\n"
			+ "\r\n"
			+ "	    float half_radius = u_radius / 2.0;\r\n"
			+ "	    vec4 pixel_color = texture2D(u_diffuse_sampler, uv);\r\n"
			+ "	    pixel_color.rgb *= pixel_color.a;\r\n"
			+ "	    pixel_color *= u_kernel[0];\r\n"
			+ "\r\n"
			+ "	    for (float f =0; f < u_radius; f++) {\r\n"
			+ "	        vec2 offset = f * u_texel_size * u_direction;\r\n"
			+ "	        vec4 left = texture2D(u_diffuse_sampler, uv - offset);\r\n"
			+ "	        vec4 right = texture2D(u_diffuse_sampler, uv + offset);\r\n"
			+ "\r\n"
			+ "	        left.rgb *= left.a;\r\n"
			+ "	        right.rgb *= right.a;\r\n"
			+ "	        pixel_color += (left + right) * u_kernel[int(f)];\r\n"
			+ "	    }\r\n"
			+ "\r\n"
			+ "	    gl_FragColor = vec4(pixel_color.rgb / pixel_color.a, pixel_color.a);\r\n"
			+ "	}");
	public static Shader bloomESP = new Shader("#version 120\r\n"
			+ "\r\n"
			+ "	uniform sampler2D u_diffuse_sampler;\r\n"
			+ "	uniform sampler2D u_other_sampler;\r\n"
			+ "	uniform vec2 u_texel_size;\r\n"
			+ "	uniform vec2 u_direction;\r\n"
			+ "	uniform vec3 color;\r\n"
			+ "	uniform float u_radius;\r\n"
			+ "	uniform float u_kernel[128];\r\n"
			+ "\r\n"
			+ "	void main(void)\r\n"
			+ "	{\r\n"
			+ "	    vec2 uv = gl_TexCoord[0].st;\r\n"
			+ "\r\n"
			+ "	    if (u_direction.x == 0.0) {\r\n"
			+ "	        float alpha = texture2D(u_other_sampler, uv).a;\r\n"
			+ "	        if (alpha > 0.0) discard;\r\n"
			+ "	    }\r\n"
			+ "\r\n"
			+ "	    float half_radius = u_radius / 2.0;\r\n"
			+ "	    vec4 pixel_color = texture2D(u_diffuse_sampler, uv);\r\n"
			+ "	    pixel_color.rgb *= pixel_color.a;\r\n"
			+ "	    pixel_color *= u_kernel[0];\r\n"
			+ "\r\n"
			+ "	    for (float f = 1; f <= u_radius; f++) {\r\n"
			+ "	        vec2 offset = f * u_texel_size * u_direction;\r\n"
			+ "	        vec4 left = texture2D(u_diffuse_sampler, uv - offset);\r\n"
			+ "	        vec4 right = texture2D(u_diffuse_sampler, uv + offset);\r\n"
			+ "\r\n"
			+ "	        left.rgb *= left.a;\r\n"
			+ "	        right.rgb *= right.a;\r\n"
			+ "	        pixel_color += (left + right) * u_kernel[int(f)];\r\n"
			+ "	    }\r\n"
			+ "\r\n"
			+ "	    gl_FragColor = vec4(color, pixel_color.a*1.8);\r\n"
			+ "	}");
	public static Shader lineESP = new Shader("#version 120\r\n"
			+ "\r\n"
			+ "	uniform sampler2D u_diffuse_sampler;\r\n"
			+ "	uniform sampler2D u_other_sampler;\r\n"
			+ "	uniform vec2 u_texel_size;\r\n"
			+ "	uniform vec2 u_direction;\r\n"
			+ "	uniform vec3 color;\r\n"
			+ "	uniform float u_radius;\r\n"
			+ "	uniform float u_kernel[128];\r\n"
			+ "\r\n"
			+ "	void main(void)\r\n"
			+ "	{\r\n"
			+ "	    vec2 uv = gl_TexCoord[0].st;\r\n"
			+ "\r\n"
			+ "	    if (u_direction.x == 0.0) {\r\n"
			+ "	        float alpha = texture2D(u_other_sampler, uv).a;\r\n"
			+ "	        if (alpha > 0.0) discard;\r\n"
			+ "	    }\r\n"
			+ "\r\n"
			+ "	    float half_radius = u_radius / 2.0;\r\n"
			+ "	    vec4 pixel_color = texture2D(u_diffuse_sampler, uv);\r\n"
			+ "	    pixel_color.rgb *= pixel_color.a;\r\n"
			+ "	    pixel_color *= u_kernel[0];\r\n"
			+ "\r\n"
			+ "	    for (float f = 1; f <= u_radius; f++) {\r\n"
			+ "	        vec2 offset = f * u_texel_size * u_direction;\r\n"
			+ "	        vec4 left = texture2D(u_diffuse_sampler, uv - offset);\r\n"
			+ "	        vec4 right = texture2D(u_diffuse_sampler, uv + offset);\r\n"
			+ "\r\n"
			+ "	        left.rgb *= left.a;\r\n"
			+ "	        right.rgb *= right.a;\r\n"
			+ "	        pixel_color += (left + right) * u_kernel[int(f)];\r\n"
			+ "	    }\r\n"
			+ "\r\n"
			+ "	    gl_FragColor = vec4(color, pixel_color.a);\r\n"
			+ "	}");


	public static Shader clientbackground = new Shader("#version 120\r\n"
			+ "uniform vec2 resolution;\r\n"
			+ "uniform float time;\r\n"
			+ "\r\n"
			+ "vec3 palette( in float t, in vec3 a, in vec3 b, in vec3 c, in vec3 d )\r\n"
			+ "{\r\n"
			+ "    return a + b*cos( 6.28318*(c*t+d) );\r\n"
			+ "}\r\n"
			+ "vec4 Line(vec2 uv, float speed, float height, vec3 col) {\r\n"
			+ "    uv.y += smoothstep(1., 0., abs(uv.x)) * sin(time * speed + sqrt(uv.x*uv.x+uv.y*uv.y) * height) * .2;\r\n"
			+ "    return vec4(smoothstep(.06 * smoothstep(.2, .9, abs(uv.x)), 0., abs(uv.y) - .004) * col, 1.0) * smoothstep(1., .3, abs(uv.x));\r\n"
			+ "}\r\n"
			+ "void main()\r\n"
			+ "{\r\n"
			+ "\r\n"
			+ "    vec2 uv = (gl_TexCoord[0].st *2. - vec2(1.))/1.;\r\n"
			+ "    gl_FragColor = vec4(0.);\r\n"
			+ "    for (float i = 0.; i <= 4.; i += 1.) {\r\n"
			+ "        float t = i/5.;\r\n"
			+ "        gl_FragColor += Line(uv *2., 1. + t, 3. + t, palette(t,vec3(0.948,-0.332,0.498),vec3(0.858,0.654,1.219),vec3(0.558,0.108,0.269),vec3(3.138,2.918,-0.082)));\r\n"
			+ "    }\r\n"
			+ "}");
	public static Shader alphaImage = new Shader("#version 120\r\n"
			+ "\r\n"
			+ "uniform float alpha;\r\n"
			+ "uniform sampler2D texture;\r\n"
			+ "\r\n"
			+ "void main()\r\n"
			+ "{\r\n"
			+ "    vec2 uv = gl_TexCoord[0].st;\r\n"
			+ "    \r\n"
			+ "    vec4 color = texture2D(texture, uv);\r\n"
			+ "    \r\n"
			+ "    if(color.a == 0.){\r\n"
			+ "        gl_FragColor = vec4(color);\r\n"
			+ "        return;\r\n"
			+ "    }\r\n"
			+ "\r\n"
			+ "    gl_FragColor = vec4(color.rgb, alpha);\r\n"
			+ "}\r\n"
			+ "");

	public static  final Shader colorPicker = new Shader(
			"#version 120\r\n"
			+ "\r\n"
			+ "#ifdef GL_ES\r\n"
			+ "precision mediump float;\r\n"
			+ "#endif\r\n"
			+ "\r\n"
			+ "uniform vec2 u_resolution;\r\n"
			+ "uniform float hue;\r\n"
			+ "\r\n"
			+ "vec3 HSBtoRGB(float hue, float saturation, float brightness)\r\n"
			+ "{\r\n"
			+ "    float chroma = brightness * saturation;\r\n"
			+ "    float huePrime = hue * 6.0;\r\n"
			+ "    float x = chroma * (1.0 - abs(mod(huePrime, 2.0) - 1.0));\r\n"
			+ "    vec3 rgbColor;\r\n"
			+ "\r\n"
			+ "    if (0.0 <= huePrime && huePrime < 1.0)\r\n"
			+ "        rgbColor = vec3(chroma, x, 0.0);\r\n"
			+ "    else if (1.0 <= huePrime && huePrime < 2.0)\r\n"
			+ "        rgbColor = vec3(x, chroma, 0.0);\r\n"
			+ "    else if (2.0 <= huePrime && huePrime < 3.0)\r\n"
			+ "        rgbColor = vec3(0.0, chroma, x);\r\n"
			+ "    else if (3.0 <= huePrime && huePrime < 4.0)\r\n"
			+ "        rgbColor = vec3(0.0, x, chroma);\r\n"
			+ "    else if (4.0 <= huePrime && huePrime < 5.0)\r\n"
			+ "        rgbColor = vec3(x, 0.0, chroma);\r\n"
			+ "    else if (5.0 <= huePrime && huePrime < 6.0)\r\n"
			+ "        rgbColor = vec3(chroma, 0.0, x);\r\n"
			+ "    else\r\n"
			+ "        rgbColor = vec3(0.0);\r\n"
			+ "\r\n"
			+ "    float m = brightness - chroma;\r\n"
			+ "    return rgbColor + vec3(m);\r\n"
			+ "}\r\n"
			+ "\r\n"
			+ "void main() {\r\n"
			+ "    vec2 cords =  gl_TexCoord[0].st;\r\n"
			+ "    gl_FragColor = vec4(HSBtoRGB(hue,cords.x,cords.y), 1.);\r\n"
			+ "}");
	public static Shader blur = new Shader("#version 120\r\n"
			+ "\r\n"
			+ "uniform sampler2D textureIn;\r\n"
			+ "uniform vec2 texelSize, direction;\r\n"
			+ "uniform float radius;\r\n"
			+ "uniform float weights[256];\r\n"
			+ "\r\n"
			+ "#define offset texelSize * direction\r\n"
			+ "\r\n"
			+ "void main() {\r\n"
			+ "    vec3 blr = texture2D(textureIn, gl_TexCoord[0].st).rgb * weights[0];\r\n"
			+ "\r\n"
			+ "    for (float f = 1.0; f <= radius; f++) {\r\n"
			+ "        blr += texture2D(textureIn, gl_TexCoord[0].st + f * offset).rgb * (weights[int(abs(f))]);\r\n"
			+ "        blr += texture2D(textureIn, gl_TexCoord[0].st - f * offset).rgb * (weights[int(abs(f))]);\r\n"
			+ "    }\r\n"
			+ "\r\n"
			+ "    gl_FragColor = vec4(blr, 1.0);\r\n"
			+ "}");
	public static  final Shader colorSlider = new Shader(
			"#version 120\r\n"
			+ "\r\n"
			+ "#ifdef GL_ES\r\n"
			+ "precision mediump float;\r\n"
			+ "#endif\r\n"
			+ "\r\n"
			+ "uniform vec2 u_resolution;\r\n"
			+ "\r\n"
			+ "vec3 HSBtoRGB(float hue, float saturation, float brightness)\r\n"
			+ "{\r\n"
			+ "    float chroma = brightness * saturation;\r\n"
			+ "    float huePrime = hue * 6.0;\r\n"
			+ "    float x = chroma * (1.0 - abs(mod(huePrime, 2.0) - 1.0));\r\n"
			+ "    vec3 rgbColor;\r\n"
			+ "\r\n"
			+ "    if (0.0 <= huePrime && huePrime < 1.0)\r\n"
			+ "        rgbColor = vec3(chroma, x, 0.0);\r\n"
			+ "    else if (1.0 <= huePrime && huePrime < 2.0)\r\n"
			+ "        rgbColor = vec3(x, chroma, 0.0);\r\n"
			+ "    else if (2.0 <= huePrime && huePrime < 3.0)\r\n"
			+ "        rgbColor = vec3(0.0, chroma, x);\r\n"
			+ "    else if (3.0 <= huePrime && huePrime < 4.0)\r\n"
			+ "        rgbColor = vec3(0.0, x, chroma);\r\n"
			+ "    else if (4.0 <= huePrime && huePrime < 5.0)\r\n"
			+ "        rgbColor = vec3(x, 0.0, chroma);\r\n"
			+ "    else if (5.0 <= huePrime && huePrime < 6.0)\r\n"
			+ "        rgbColor = vec3(chroma, 0.0, x);\r\n"
			+ "    else\r\n"
			+ "        rgbColor = vec3(0.0);\r\n"
			+ "\r\n"
			+ "    float m = brightness - chroma;\r\n"
			+ "    return rgbColor + vec3(m);\r\n"
			+ "}\r\n"
			+ "\r\n"
			+ "void main() {\r\n"
			+ "    vec2 cords =  gl_TexCoord[0].st;\r\n"
			+ "    gl_FragColor=vec4(HSBtoRGB(cords.x,1.,1.0),1.);\r\n"
			+ "}");

    
}
