namespace Citylogia.Server.Models
{
    public class Coord
    {
        public Coord(double x, double y)
        {
            X = x;
            Y = y;
        }

        public double X { get; set; }
        public double Y { get; set; }
    }
}
