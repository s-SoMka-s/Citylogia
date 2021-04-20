using Microsoft.EntityFrameworkCore.Migrations;

namespace Core.Db.Migrations
{
    public partial class Changereviewplacelink : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropIndex(
                name: "IX_Reviews_PlaceId",
                schema: "citylogia",
                table: "Reviews");

            migrationBuilder.DropIndex(
                name: "IX_Reviews_UserId",
                schema: "citylogia",
                table: "Reviews");

            migrationBuilder.CreateIndex(
                name: "IX_Reviews_PlaceId",
                schema: "citylogia",
                table: "Reviews",
                column: "PlaceId");

            migrationBuilder.CreateIndex(
                name: "IX_Reviews_UserId",
                schema: "citylogia",
                table: "Reviews",
                column: "UserId");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropIndex(
                name: "IX_Reviews_PlaceId",
                schema: "citylogia",
                table: "Reviews");

            migrationBuilder.DropIndex(
                name: "IX_Reviews_UserId",
                schema: "citylogia",
                table: "Reviews");

            migrationBuilder.CreateIndex(
                name: "IX_Reviews_PlaceId",
                schema: "citylogia",
                table: "Reviews",
                column: "PlaceId",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_Reviews_UserId",
                schema: "citylogia",
                table: "Reviews",
                column: "UserId",
                unique: true);
        }
    }
}
