"""Approved elephant grass stage 0, variant B: inclined young blades.
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
    # One symbol is one pixel; the tuft begins on row 6.
    rows = ['..........o...', '.........omo..', '........ohso..', '..oo....ohso..', '...hmo.ohso...', '....hmoohso...', '.....hmohso...', '..oo..ohmso...', '...hmoohso....', '.....osso.....']
    stamp(rows,1,6)
    return image
if __name__ == '__main__':
    generate().save(Path(__file__).with_suffix('.png'))
