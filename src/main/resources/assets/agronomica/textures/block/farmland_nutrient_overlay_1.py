"""Approved nutrient overlay level 1, variant C: muted olive mineral grains.
Sparse authored patches, hard alpha, no antialiasing or random dithering.
Apply over the vanilla farmland top texture. Run to regenerate its PNG.
Edit PALETTE and PIXELS for future adjustments.
"""
from pathlib import Path
from PIL import Image
PALETTE = {'.': (0, 0, 0, 0), 'o': (89, 82, 44, 255), 'm': (141, 134, 75, 255), 'h': (176, 163, 103, 255)}
PIXELS = ['................', '..hm............', '..o.............', '............hm..', '.............o..', '.......hm.......', '.......o........', '................', '................', '.hm.............', '.o........hm....', '..........o.....', '................', '................', '.....hm.........', '......o.........']
def generate():
    assert len(PIXELS)==16 and all(len(row)==16 for row in PIXELS)
    image=Image.new('RGBA',(16,16))
    image.putdata([PALETTE[c] for row in PIXELS for c in row])
    return image
if __name__ == '__main__':
    generate().save(Path(__file__).with_suffix('.png'))
