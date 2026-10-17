"""Approved elephant grass stage 3, variant C: green tuft with straw-colored lower blades. Hand-authored pixels, Pillow only.
One 16x16 RGBA face for the Minecraft cross model; base on the bottom row.
Hard alpha, no antialiasing, three green surface tones plus a dark outline.
Run to regenerate the PNG beside this script.
"""
from pathlib import Path
from PIL import Image
PALETTE={'.':(0,0,0,0),'o':(39,61,24,255),
         's':(66,103,32,255),'m':(110,151,43,255),'h':(169,191,76,255),
         'c':(101,79,42,255),'t':(167,137,70,255),'p':(213,190,116,255)}
# A slender feathered head, with a continuous pale inner ridge.
HEAD=["..c.",".tpc","ctpc",".tpc","ctpc",".tcc","..c."]
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
    rows = ['.........o......', '........omo.....', '.......ohso.....', '...ooo.ohso.....', '....hmohso.ooo..', '.....hmhsoohmo..', '.ooo..ohmohso...', '..hmo.ohmhso.oo.', '...hmoohmsoohmo.', '....hmohmshhoo..', 'o....ohmhshshmo.', 'ctc..chmhso.ohmo', '.ptc.chmhsocptc.', '..ptcpmmhsopcc..', '...cptmhmhhcc...', '....ccsssssc....']
    stamp(rows,0,0)
    stamp(['.c.','ctc','cpc','.tc','.c.'],7,10)
    return image
if __name__ == '__main__':
    generate().save(Path(__file__).with_suffix('.png'))
