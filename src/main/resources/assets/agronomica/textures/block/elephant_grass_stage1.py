"""Approved elephant grass stage 1, variant B: upright blade and arched side leaves. Hand-authored pixels, Pillow only.
One 16x16 RGBA face for the Minecraft cross model; base on the bottom row.
Hard alpha, no antialiasing, three green surface tones plus a dark outline.
Run to regenerate the PNG beside this script.
"""
from pathlib import Path
from PIL import Image
PALETTE={'.':(0,0,0,0),'o':(39,61,24,255),
         's':(66,103,32,255),'m':(110,151,43,255),'h':(169,191,76,255)}
def generate():
    image=Image.new('RGBA',(16,16),(0,0,0,0))
    def stamp(rows,x,y,mirror=False):
        assert len(set(map(len,rows)))==1
        sprite=Image.new('RGBA',(len(rows[0]),len(rows)))
        sprite.putdata([PALETTE[c] for row in rows for c in row])
        if mirror:
            sprite=sprite.transpose(Image.Transpose.FLIP_LEFT_RIGHT)
        assert x>=0 and y>=0 and x+sprite.width<=16 and y+sprite.height<=16
        image.alpha_composite(sprite,(x,y))
    # Each symbol is one pixel. The young tuft begins on row 2.
    rows = ['......o.......', '.....omo......', '.....ohso.....', '.....ohso.....', '.ooo.ohso.ooo.', '..hmoohsoohmo.', '...hmohsohso..', 'o...hmhshso...', 'omo..ohmhso...', '.hmo.ohmso..o.', '..hmoohmsoomo.', '...hmohmshso..', '....ohmhso....', '.....ossso....']
    stamp(rows,1,2)
    return image
if __name__ == '__main__':
    generate().save(Path(__file__).with_suffix('.png'))
