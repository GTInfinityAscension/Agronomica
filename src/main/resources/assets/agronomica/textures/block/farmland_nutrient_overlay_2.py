"""Approved nutrient overlay level 2, variant C: diagonal mineral traces.
Retains all level-1 mineral sites and adds a denser, deliberately drawn pattern.
Pillow only, hard alpha, no antialiasing, no random dithering.
Run to regenerate the PNG beside this script.
"""
from pathlib import Path
from PIL import Image
PALETTE = {'.': (0, 0, 0, 0), 'o': (89, 82, 44, 255), 'm': (141, 134, 75, 255), 'h': (176, 163, 103, 255)}
PIXELS = ['................', '..hm............', '..ohm.....hm....', '....mo.....mom..', '.............o..', '.......hm.......', '.......ohm......', '.........mo.....', '................', '.hm.............', '.ohm......hm....', '...mo.....ohm...', '............mo..', '......hm........', '.....hmmo.......', '......o.........']
def generate():
    assert len(PIXELS)==16 and all(len(row)==16 for row in PIXELS)
    image=Image.new('RGBA',(16,16))
    image.putdata([PALETTE[c] for row in PIXELS for c in row])
    return image
if __name__ == '__main__':
    generate().save(Path(__file__).with_suffix('.png'))
