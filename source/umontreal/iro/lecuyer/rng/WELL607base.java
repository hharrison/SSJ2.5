package umontreal.iro.lecuyer.rng;

import umontreal.iro.lecuyer.util.BitVector;
import umontreal.iro.lecuyer.util.BitMatrix;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.InputStream;

import java.io.FileNotFoundException;
import java.io.IOException;


abstract class WELL607base extends RandomStreamBase {

   private static final long serialVersionUID = 70510L;
   // La date de modification a l'envers, lire 10/05/2007

   // constants
   static final double NORM = (1.0 / 0x100000001L);
   // static final double NORM = 2.32830643653869628906e-10;

   static final int R = 19;       // useful length of the state
   static final int BUFFER_SIZE = 32; // length of the state
   static final int NUM_BITS = 608;
   static final int MASK_STATE = 0x0000001F; // = 31
   static final int W = 32;
   static final int P = 1;
   static final int MASKU = (0xffffffff >>> (W - P));
   static final int MASKL = (~MASKU);
   static final int M1 = 16;
   static final int M2 = 15;
   static final int M3 = 14;
   static final int R1 = 18;      // R - 1
   static final int R2 = 17;      // R - 2

   // state variables
   int state_i;
   int[] state;

   // stream and substream
   int[] stream;
   int[] substream;

   // length of the jumps
   static final int w = 250;
   static final int v = 150;

   // the state transition matrices
   static BitMatrix Apw, Apz;

   // if the generator was initialised (transition matrices)
   private static boolean initialised = false;

   static void initialisation () {
      // read the state transition matrices
      if (initialised)
         return ;

      try {
         InputStream is = WELL607base.class.getClassLoader ().
                          getResourceAsStream ("umontreal/iro/lecuyer/rng/WELL607.dat");
         ObjectInputStream ois = new ObjectInputStream (is);

         Apw = (BitMatrix) ois.readObject ();
         Apz = (BitMatrix) ois.readObject ();

         ois.close ();
      } catch (FileNotFoundException e) {
         System.err.println ("Couldn't find WELL607.dat");
         System.err.println (e);

         System.exit (1);
      } catch (IOException e) {
         System.err.println (e);

         System.exit (1);
      } catch (ClassNotFoundException e) {
         System.err.println (e);

         System.exit (1);
      }

      initialised = true;
   }

/*
   // advance the state by a transition matrice
   static void advanceSeed(int[] seed, BitMatrix bm) {
      BitVector bv = new BitVector(seed, NUM_BITS);
      bv = bm.multiply(bv);
      for(int i = 0; i < R; i++)
         seed[i] = bv.getInt(i);
   }
*/

   // advance the state by a transition matrice
   protected void advanceSeed(int[] seed, int [] p) {
      int b;
      int[] x = new int[R];

      for (int i = 0; i < R; i++) {
         state[i] = seed[i];
      }
      state_i = 0;

      for (int j = 0; j < R; ++j) {
         b = p[j];
         for (int k = 0; k < W; ++k) {
            if ((b & 1) == 1) {
               for (int i = 0; i < R; i++) {
                  x[i] ^= state[(state_i + i) & MASK_STATE];
               }
            }
            b >>= 1;
            nextInt ();
         }
      }

      for (int i = 0; i < R; i++) {
         seed[i] = x[i];
      }
   }


   static void verifySeed (int seed[]) {
      if (seed.length < R)
         throw new IllegalArgumentException ("Seed must contain " + R +
                                             " values");

      boolean goodSeed = false;
      for (int i = 0; !goodSeed && i < R; i++)
         if (seed[i] != 0)
            goodSeed = true;
      if (!goodSeed)
         if (seed[R - 1] == 0x80000000)
            throw new IllegalArgumentException
            ("At least one of the element of the seed must not be 0. " +
             "If this element is the last one, it mustn't be equal " +
             "to 0x80000000 (" + 0x80000000 + ").");
   }


   int[] getState () {
      int[] result = new int[R];
      for (int i = 0; i < R; i++)
         result[i] = state[(state_i + i) & MASK_STATE];
      return result;
   }


   // just like formatState, but not public
   String stringState () {
      StringBuffer sb = new StringBuffer ();
      for (int i = 0; i < R - 1; i++)
         sb.append (state[(state_i + i) & MASK_STATE] + ", ");
      sb.append (state[(state_i + R - 1) & MASK_STATE] + "}");
      return sb.toString ();
   }


   int nextInt () {
      int z0, z1, z2;

      z0 = (state[(state_i + R1) & MASK_STATE] & MASKL) |
           (state[(state_i + R2) & MASK_STATE] & MASKU);
      z1 = (state[state_i] ^ (state[state_i] >>> 19)) ^
           (state[(state_i + M1) & MASK_STATE] ^
            (state[(state_i + M1) & MASK_STATE] >>> 11));
      z2 = (state[(state_i + M2) & MASK_STATE] ^
            (state[(state_i + M2) & MASK_STATE] << (14))) ^
           state[(state_i + M3) & MASK_STATE];
      state[state_i] = z1 ^ z2;
      state[(state_i - 1) & MASK_STATE] = (z0 ^ (z0 >>> 18)) ^
                                          z1 ^ (state[state_i] ^ (state[state_i] << 5));

      state_i--;
      state_i &= MASK_STATE;
      return state[state_i];
   }


   /*  SENT TO WELL607
   public static void main (String[]args) {
      if (args.length < 1) {
         System.err.println ("Must provide the output file.");
         System.exit (1);
      }
      // computes the state transition matrices
      System.out.println ("Creating the WELL607 state transition matrices.");

      // the state transition matrices
      BitMatrix STp0, STpw, STpz;

      BitVector[]bv = new BitVector[NUM_BITS];

      int[] state = new int[BUFFER_SIZE];
      int[] vect = new int[R];

      int z0, z1, z2;

      for (int i = 0; i < NUM_BITS; i++) {
         // state is a unit vector
         for (int j = 0; j < BUFFER_SIZE; j++)
            state[j] = 0;
         state[i / W] = 1 << (i % W);


         // advance one state of the recurrence (state_i = 0)
         z0 = (state[R1] & MASKL) | (state[R2] & MASKU);
         z1 =
            (state[0] ^ (state[0] >>> 19)) ^ (state[M1] ^ (state[M1] >>> 11));
         z2 = (state[M2] ^ (state[M2] << (14))) ^ state[M3];
         state[0] = z1 ^ z2;
         state[BUFFER_SIZE - 1] = (z0 ^ (z0 >>> 18)) ^
                                  z1 ^ (state[0] ^ (state[0] << 5));


         // put the state vector in vect (state_i = -1)
         for (int j = 0; j < R; j++)
            vect[j] = state[(j - 1) & MASK_STATE];

         bv[i] = new BitVector (vect, NUM_BITS);
      }

      STp0 = (new BitMatrix (bv)).transpose ();

      STpw = STp0.power2e (w);
      STpz = STpw.power2e (v);

      try {
         FileOutputStream fos = new FileOutputStream (args[0]);
         ObjectOutputStream oos = new ObjectOutputStream (fos);
         oos.writeObject (STpw);
         oos.writeObject (STpz);
         oos.close ();
      } catch (FileNotFoundException e) {
         System.err.println ("Couldn't create " + args[0]);
         System.err.println (e);
      } catch (IOException e) {
         System.err.println (e);
      }

   }
    */

   public WELL607base clone () {
      WELL607base retour = null;
      retour = (WELL607base) super.clone ();
      return retour;
   }
}
