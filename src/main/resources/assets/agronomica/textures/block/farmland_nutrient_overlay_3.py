"""Approved nutrient overlay level 3, variant B: broad olive mineral deposits.
Retains level-2 mineral sites and the approved olive palette.
Pillow only, binary alpha, no antialiasing or random dithering.
Run to regenerate the PNG beside this script.
"""
from pathlib import Path
from PIL import Image
PALETTE = {'.': (0, 0, 0, 0), 'o': (89, 82, 44, 255), 'm': (141, 134, 75, 255), 'h': (176, 163, 103, 255)}
PIXELS = ['..hm............', '..hmm...........', '..ohmo....hmhm..', '....mo.....momm.', '......hm.....oo.', '.......mm.......', '.......ohm......', '........moo.....', 'hm..............', '.mm......hm.....', '.ohm......mm....', '..moo.....ohm...', '....hm.....moo..', '.....mmm........', '.....hmmo.......', '......mo........']
def generate():
    assert len(PIXELS)==16 and all(len(row)==16 for row in PIXELS)
    image=Image.new('RGBA',(16,16))
    image.putdata([PALETTE[c] for row in PIXELS for c in row])
    return image
if __name__ == '__main__':
    generate().save(Path(__file__).with_suffix('.png'))
